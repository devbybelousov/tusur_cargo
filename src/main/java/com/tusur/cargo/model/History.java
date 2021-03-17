package com.tusur.cargo.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class History {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long historyId;

  @ManyToOne
  private User companion;

  @ManyToMany
  List<Message> messages;
}
