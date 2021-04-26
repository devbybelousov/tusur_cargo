package com.tusur.cargo.dto;

import com.tusur.cargo.model.Size;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  @NotBlank
  @javax.validation.constraints.Size(max = 50)
  private String title;

  @javax.validation.constraints.Size(max = 250)
  private String description;

  @NotBlank
  @javax.validation.constraints.Size(max = 30)
  private String type;

  @javax.validation.constraints.Size(max = 100)
  private String addressSender;
  @javax.validation.constraints.Size(max = 100)
  private String addressRecipient;
  @Max(1000000)
  private Double price;
  private Instant departDate;
  private Instant arrivalDate;
  @Valid
  private Size size;
  private List<Long> imagesId;
}
