package com.tusur.cargo.model;

import com.tusur.cargo.enumeration.MessageStatus;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String chatId;

  @Size(max = 250)
  private String content;
  private Date timestamp;
  private MessageStatus status;

  @ManyToOne
  @JoinColumn(name = "senderId", referencedColumnName = "userId")
  private User sender;

  @ManyToOne
  @JoinColumn(name = "recipientId", referencedColumnName = "userId")
  private User recipient;

}
