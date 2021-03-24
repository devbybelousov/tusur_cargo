package com.tusur.cargo.service;

import com.tusur.cargo.dto.OrderRequest;

public interface OrderService {

  short createOrder(OrderRequest orderRequest);

}
