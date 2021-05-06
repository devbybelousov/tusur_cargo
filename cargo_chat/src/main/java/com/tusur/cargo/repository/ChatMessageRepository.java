package com.tusur.cargo.repository;

import com.tusur.cargo.enumeration.MessageStatus;
import com.tusur.cargo.model.ChatMessage;
import com.tusur.cargo.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  long countBySenderAndRecipientAndStatus(
      User senderId, User recipientId, MessageStatus status);

  List<ChatMessage> findByChatId(String chatId);

  List<ChatMessage> findAllBySenderAndRecipientAndStatus(User senderId, User recipientId,
      MessageStatus status);
}
