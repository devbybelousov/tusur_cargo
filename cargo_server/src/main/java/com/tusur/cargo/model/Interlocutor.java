package com.tusur.cargo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Interlocutor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private User interlocutor;

  @OneToOne
  private Order order;

  public Interlocutor(User interlocutor, Order order) {
    this.interlocutor = interlocutor;
    this.order = order;
  }
}
