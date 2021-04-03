package com.tusur.cargo.repository;

import com.tusur.cargo.model.RecipientMessage;
import com.tusur.cargo.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientMessageRepository extends JpaRepository<RecipientMessage, Long> {

  List<RecipientMessage> findAllByRecipient(User recipient);
}
