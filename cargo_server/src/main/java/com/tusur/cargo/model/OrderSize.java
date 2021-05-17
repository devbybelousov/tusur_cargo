package com.tusur.cargo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "size")
public class OrderSize {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long sizeId;

  @Max(10000)
  @ApiModelProperty(notes = "Ширина",
      example = "2.1", required = false)
  private double width;

  @Max(10000)
  @ApiModelProperty(notes = "Высота",
      example = "2.1", required = false)
  private double height;

  @Max(10000)
  @ApiModelProperty(notes = "Длина",
      example = "2.1", required = false)
  private double length;

  @Max(10000)
  @ApiModelProperty(notes = "Вес",
      example = "2.1", required = false)
  private double weight;
}
