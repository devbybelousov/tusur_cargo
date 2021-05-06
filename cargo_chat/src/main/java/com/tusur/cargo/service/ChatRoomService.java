package com.tusur.cargo.service;

import java.util.Optional;

public interface ChatRoomService {

  Optional<String> getChatId(
      Long senderId, Long recipientId, boolean createIfNotExist);

  Boolean existChatRoom(Long senderId, Long recipientId);
}
