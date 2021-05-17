package com.tusur.cargo.dto;

import com.tusur.cargo.model.OrderSize;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  @NotBlank
  @Size(max = 50)
  @ApiModelProperty(notes = "Название объявления",
      example = "Название", required = true)
  private String title;

  @Size(max = 250)
  @ApiModelProperty(notes = "Описание объявления",
      example = "Описание", required = false)
  private String description;

  @NotBlank
  @Size(max = 30)
  @ApiModelProperty(notes = "Тип объявления",
      example = "CARGO", required = true)
  private String type;

  @Size(max = 100)
  @ApiModelProperty(notes = "Адрес отправителя",
      example = "Москва", required = false)
  private String addressSender;

  @Size(max = 100)
  @ApiModelProperty(notes = "Адрес получателя",
      example = "Москва", required = false)
  private String addressRecipient;

  @Max(1000000)
  @ApiModelProperty(notes = "Цена объявления",
      example = "1234", required = false)
  private Double price;

  @ApiModelProperty(notes = "Дата отправления",
      example = "20.05.2021", required = false)
  private Instant departDate;

  @ApiModelProperty(notes = "Дата получения",
      example = "20.05.2021", required = false)
  private Instant arrivalDate;

  @Valid
  @ApiModelProperty(notes = "Размер", required = false)
  private OrderSize orderSize;

  @ApiModelProperty(notes = "Список идентификаторов изображений", required = false)
  private List<Long> imagesId;
}
