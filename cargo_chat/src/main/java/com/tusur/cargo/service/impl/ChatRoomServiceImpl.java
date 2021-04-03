package com.tusur.cargo.service.impl;

import com.tusur.cargo.model.ChatRoom;
import com.tusur.cargo.repository.ChatRoomRepository;
import com.tusur.cargo.service.ChatRoomService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  @Override
  public Optional<String> getChatId(
      Long senderId, Long recipientId, boolean createIfNotExist) {
    
    return chatRoomRepository
        .findBySenderIdAndRecipientId(senderId, recipientId)
        .map(ChatRoom::getChatId)
        .or(() -> {
          if (!createIfNotExist) {
            return Optional.empty();
          }
          var chatId =
              String.format("%s_%s", senderId, recipientId);

          ChatRoom senderRecipient = ChatRoom
              .builder()
              .chatId(chatId)
              .senderId(senderId)
              .recipientId(recipientId)
              .build();

          ChatRoom recipientSender = ChatRoom
              .builder()
              .chatId(chatId)
              .senderId(recipientId)
              .recipientId(senderId)
              .build();
          chatRoomRepository.save(senderRecipient);
          chatRoomRepository.save(recipientSender);

          return Optional.of(chatId);
        });
  }

  @Override
  public Boolean existChatRoom(Long senderId, Long recipientId) {
    return chatRoomRepository.existsBySenderIdAndRecipientId(senderId, recipientId);
  }
}
