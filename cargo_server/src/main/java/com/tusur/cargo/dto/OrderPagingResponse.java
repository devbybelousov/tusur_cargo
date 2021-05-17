package com.tusur.cargo.dto;

import com.tusur.cargo.model.Order;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPagingResponse {

  private Long count;

  private Long pageNumber;

  private Long pageSize;

  private Long pageOffset;

  private Long pageTotal;

  private List<Order> elements;
}
