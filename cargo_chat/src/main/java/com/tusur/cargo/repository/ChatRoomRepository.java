package com.tusur.cargo.repository;

import com.tusur.cargo.model.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

  Boolean existsBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
