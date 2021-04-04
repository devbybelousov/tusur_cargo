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
public class AdminRequest {

  @NotBlank
  @Email
  private String email;
  @NotBlank
  @Size(min = 6, max = 20)
  private String password;
}
