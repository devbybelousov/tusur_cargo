package com.tusur.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private Long id;

  private String name;

  private String email;

  private int countOrders;

  private int countFeedbacks;

  private double rating;
}
