package com.tusur.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {
  private Long id;
  private String name;
  private String email;
  private int countAccepted;
  private int countRejected;
  private int countAll;
}
