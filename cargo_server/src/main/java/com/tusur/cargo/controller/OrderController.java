package com.tusur.cargo.controller;

import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.service.OrderService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(orderService.createOrder(orderRequest));
  }


}
