package com.tusur.cargo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Content {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long contentId;

  @NotBlank(message = "Body is required")
  private String body;
}
