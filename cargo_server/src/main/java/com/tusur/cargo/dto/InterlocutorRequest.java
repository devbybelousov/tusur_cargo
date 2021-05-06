package com.tusur.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterlocutorRequest {

  private Long userId;
  private Long orderId;
  private Long interlocutorId;
}
