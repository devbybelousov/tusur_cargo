package com.tusur.cargo.repository;

import com.tusur.cargo.model.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  long countBySenderIdAndRecipientIdAndStatus(
      Long senderId, Long recipientId, String status);

  List<ChatMessage> findByChatId(String chatId);

  List<ChatMessage> findAllBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, String status);
}
