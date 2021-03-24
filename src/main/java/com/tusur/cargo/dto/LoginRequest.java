package com.tusur.cargo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  @NotBlank
  @Size(max = 30)
  @Email
  private String email;

  @NotBlank
  @Size(max = 25)
  private String password;
}
