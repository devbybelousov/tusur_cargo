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
public class SignupRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Size(max = 40)
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 20)
  private String password;

  @NotBlank(message = "Name is required")
  @Size(max = 30)
  private String name;
}
