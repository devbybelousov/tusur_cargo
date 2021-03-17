package com.tusur.cargo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Size {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sizeId;

  private int width;
  private int height;
  private int length;
  private int weight;
}
