package com.tusur.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipientMessageRequest {

  private Long userId;
  private Long orderId;
  private Long recipientId;
}
