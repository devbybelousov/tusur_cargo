package com.tusur.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdminResponse {
  private Long id;
  private String name;
  private String email;
  private int countAccept;
  private int countReject;
  private int countAll;

  public AdminResponse(Long id, String name, String email, int countAccept, int countReject) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.countAccept = countAccept;
    this.countReject = countReject;
    this.countAll = countAccept + countReject;
  }
}
