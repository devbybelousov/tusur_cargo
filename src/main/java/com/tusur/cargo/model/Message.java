package com.tusur.cargo.model;

import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageId;

  @OneToOne
  private Content content;

  @ManyToOne(cascade = CascadeType.MERGE)
  private User from;

  @ManyToOne(cascade = CascadeType.MERGE)
  private User to;
  private Instant created;
}
