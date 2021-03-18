package com.tusur.cargo.model;

import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @NotBlank(message = "Type is required")
  private String type;

  @NotBlank(message = "Title is required")
  private String title;

  private String description;
  private String addressSender;
  private String addressRecipient;
  private int price;
  private Instant departDate;
  private Instant arrivalDate;

  @OneToMany
  @JoinColumn(name = "orderId", referencedColumnName = "orderId")
  private List<Photo> photos;

  @OneToOne
  private Size size;
}

