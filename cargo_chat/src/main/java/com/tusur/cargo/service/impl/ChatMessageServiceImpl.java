package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.ChatRequest;
import com.tusur.cargo.enumeration.MessageStatus;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.exception.ResourceNotFoundException;
import com.tusur.cargo.model.ChatMessage;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.ChatMessageRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.ChatMessageService;
import com.tusur.cargo.service.ChatRoomService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

  private final ChatMessageRepository messageRepository;
  private final ChatRoomService chatRoomService;
  private final UserRepository userRepository;

  @Override
  public ChatMessage save(ChatRequest chatRequest) {
    var chatId = chatRoomService
        .getChatId(
            chatRequest.getSenderId(),
            chatRequest.getRecipientId(),
            !chatRoomService.existChatRoom(
                chatRequest.getSenderId(),
                chatRequest.getRecipientId()));

    User sender = userRepository.findByUserId(chatRequest.getSenderId())
        .orElseThrow(
            () -> new NotFoundException("User not found with id - " + chatRequest.getSenderId()));

    User recipient = userRepository.findByUserId(chatRequest.getRecipientId())
        .orElseThrow(() -> new NotFoundException(
            "User not found with id - " + chatRequest.getRecipientId()));

    ChatMessage chatMessage = new ChatMessage().toBuilder()
        .chatId(chatId.get())
        .sender(sender)
        .recipient(recipient)
        .timestamp(new Date())
        .content(chatRequest.getContent())
        .status(MessageStatus.RECEIVED)
        .build();

    messageRepository.save(chatMessage);
    return chatMessage;
  }

  @Override
  public long countNewMessages(Long senderId, Long recipientId) {
    User sender = userRepository.findByUserId(senderId)
        .orElseThrow(() -> new NotFoundException("User not found with id - " + senderId));

    User recipient = userRepository.findByUserId(recipientId)
        .orElseThrow(() -> new NotFoundException("User not found with id - " + recipientId));

    return messageRepository.countBySenderAndRecipientAndStatus(
        sender, recipient, MessageStatus.RECEIVED);
  }

  @Override
  public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
    var chatId = chatRoomService.getChatId(senderId, recipientId, false);

    var messages =
        chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());

    if (messages.size() > 0) {
      updateStatus(senderId, recipientId, MessageStatus.DELIVERED);
    }

    return messages;
  }

  @Override
  public ChatMessage findById(Long id) {
    return messageRepository
        .findById(id)
        .map(chatMessage -> {
          chatMessage.setStatus(MessageStatus.DELIVERED);
          return messageRepository.save(chatMessage);
        })
        .orElseThrow(() ->
            new ResourceNotFoundException("can't find message (" + id + ")"));
  }

  @Override
  public void updateStatus(Long senderId, Long recipientId, MessageStatus status) {
    User sender = userRepository.findByUserId(senderId)
        .orElseThrow(() -> new NotFoundException("User not found with id - " + senderId));

    User recipient = userRepository.findByUserId(recipientId)
        .orElseThrow(() -> new NotFoundException("User not found with id - " + recipientId));

    messageRepository.findAllBySenderAndRecipientAndStatus(sender, recipient,
        MessageStatus.RECEIVED).forEach(chatMessage -> {
      chatMessage.setStatus(status);
      messageRepository.save(chatMessage);
    });
  }
}
