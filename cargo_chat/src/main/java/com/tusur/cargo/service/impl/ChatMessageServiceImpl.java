package com.tusur.cargo.service.impl;

import com.tusur.cargo.enumiration.MessageStatus;
import com.tusur.cargo.exception.ResourceNotFoundException;
import com.tusur.cargo.model.ChatMessage;
import com.tusur.cargo.repository.ChatMessageRepository;
import com.tusur.cargo.service.ChatMessageService;
import com.tusur.cargo.service.ChatRoomService;
import java.util.ArrayList;
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

  @Override
  public ChatMessage save(ChatMessage chatMessage) {
    chatMessage.setStatus(MessageStatus.RECEIVED.toString());
    messageRepository.save(chatMessage);
    return chatMessage;
  }

  @Override
  public long countNewMessages(Long senderId, Long recipientId) {
    return messageRepository.countBySenderIdAndRecipientIdAndStatus(
        senderId, recipientId, MessageStatus.RECEIVED.toString());
  }

  @Override
  public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
    var chatId = chatRoomService.getChatId(senderId, recipientId, true);

    var messages =
        chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());

    if (messages.size() > 0) {
      updateStatus(senderId, recipientId, MessageStatus.DELIVERED.toString());
    }

    return messages;
  }

  @Override
  public ChatMessage findById(Long id) {
    return messageRepository
        .findById(id)
        .map(chatMessage -> {
          chatMessage.setStatus(MessageStatus.DELIVERED.toString());
          return messageRepository.save(chatMessage);
        })
        .orElseThrow(() ->
            new ResourceNotFoundException("can't find message (" + id + ")"));
  }

  @Override
  public void updateStatus(Long senderId, Long recipientId, String status) {
    messageRepository.findAllBySenderIdAndRecipientIdAndStatus(senderId, recipientId,
        MessageStatus.RECEIVED.toString()).stream().forEach(chatMessage -> {
          chatMessage.setStatus(status);
          messageRepository.save(chatMessage);
    });
  }
}
