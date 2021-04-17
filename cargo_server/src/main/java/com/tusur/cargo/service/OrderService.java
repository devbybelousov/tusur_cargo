package com.tusur.cargo.service;

import com.tusur.cargo.dto.OrderPagingResponse;
import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.model.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;

public interface OrderService {

  short createOrder(OrderRequest orderRequest);

  List<Order> getAllOrderByType(String type);

  OrderPagingResponse getAllOrder(Specification<Order> spec, HttpHeaders headers, Sort sort);

  OrderPagingResponse getAllOrder(Specification<Order> spec, Pageable pageable);

  List<Order> getAllOrder(Specification<Order> spec, Sort sort);

  List<Order> getAllOrderByStatusChecked();

  Order getOrder(Long id);

  short editOrder(OrderRequest orderRequest, Long id);

  short deleteOrder(Long id);

  short updateStatus(Long id, String status);
}
