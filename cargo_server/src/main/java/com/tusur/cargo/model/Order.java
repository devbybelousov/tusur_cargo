package com.tusur.cargo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tusur.cargo.enumeration.OrderStatus;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@Builder(toBuilder = true)
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

  private Double price;

  private Date departDate;

  private Date arrivalDate;

  @ManyToMany
  @JoinTable(name = "order_photo",
      joinColumns = @JoinColumn(name = "orderId"),
      inverseJoinColumns = @JoinColumn(name = "photoId")
  )
  private List<Photo> photos;

  @OneToOne
  private OrderSize orderSize;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;

  private Date createdAt;

  private Date deletedAt;

  @Size(max = 50)
  private OrderStatus status;

  public Order(String type, String title, String description, String addressSender,
      String addressRecipient, Double price, Date departDate, Date arrivalDate,
      OrderSize orderSize,
      List<Photo> photos) {
    this.type = type;
    this.title = title;
    this.description = description;
    this.addressSender = addressSender;
    this.addressRecipient = addressRecipient;
    this.price = price;
    this.departDate = departDate;
    this.arrivalDate = arrivalDate;
    this.orderSize = orderSize;
    this.photos = photos;
    createdAt = new Date();
    status = OrderStatus.CHECK;
  }
}

