package com.tusur.cargo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

  @NotBlank
  @Size(min = 6, max = 12)
  private String oldPassword;
  @NotBlank
  @Size(min = 6, max = 12)
  private String newPassword;
  private Long id;
}
