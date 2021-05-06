package com.tusur.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {

  private Long senderId;
  private Long recipientId;
  private String content;
}
