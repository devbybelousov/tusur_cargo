package com.tusur.cargo.service;

import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.model.Order;
import java.util.List;

public interface OrderService {

  short createOrder(OrderRequest orderRequest);

  List<Order> getAllOrderByType(String type);

  List<Order> getAllOrderByStatusChecked();

  Order getOrder(Long id);

  short editOrder(OrderRequest orderRequest, Long id);

  short deleteOrder(Long id);

  short updateStatus(Long id, String status);
}
