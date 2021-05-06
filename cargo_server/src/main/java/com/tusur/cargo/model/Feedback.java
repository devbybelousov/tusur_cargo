package com.tusur.cargo.model;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Builder(toBuilder = true)
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long feedbackId;

  @ManyToOne
  private User author;
  @NotBlank
  @Size(max = 50)
  private String authorName;
  private double rating;
  private String content;

  @ManyToOne
  private Order order;
  private Instant created_at;

}
