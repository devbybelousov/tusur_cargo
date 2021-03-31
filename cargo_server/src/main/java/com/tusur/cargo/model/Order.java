package com.tusur.cargo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;
  private Instant created;

  public Order(String type, String title, String description, String addressSender,
      String addressRecipient, int price, Instant departDate, Instant arrivalDate, Size size,
      List<Photo> photos) {
    this.type = type;
    this.title = title;
    this.description = description;
    this.addressSender = addressSender;
    this.addressRecipient = addressRecipient;
    this.price = price;
    this.departDate = departDate;
    this.arrivalDate = arrivalDate;
    this.size = size;
    this.photos = photos;
  }
}

