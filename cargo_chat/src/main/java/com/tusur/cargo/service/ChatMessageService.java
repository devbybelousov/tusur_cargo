package com.tusur.cargo.service;

import com.tusur.cargo.model.ChatMessage;
import java.util.List;

public interface ChatMessageService {

  ChatMessage save(ChatMessage chatMessage);

  long countNewMessages(Long senderId, Long recipientId);

  List<ChatMessage> findChatMessages(Long senderId, Long recipientId);

  ChatMessage findById(Long id);

  void updateStatus(Long senderId, Long recipientId, String status);
}
