package com.tusur.cargo.dto;

import io.swagger.annotations.ApiModelProperty;
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
  @Size(max = 40)
  @ApiModelProperty(notes = "Почта администратора",
      example = "admin@example.com", required = true)
  private String email;

  @NotBlank
  @Size(min = 6, max = 12)
  @ApiModelProperty(notes = "Пароль администратора",
      example = "Dfg4fs", required = true)
  private String password;
}
