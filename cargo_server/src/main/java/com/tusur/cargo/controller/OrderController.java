package com.tusur.cargo.controller;

import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.enumiration.OrderType;
import com.tusur.cargo.service.OrderService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(orderService.createOrder(orderRequest));
  }

  @GetMapping("/cargo")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> getAllOrdersByCargo() {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrderByType(OrderType.CARGO.toString()));
  }

  @GetMapping("/carrier")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> getAllOrdersByCarrier() {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrderByType(OrderType.CARRIER.toString()));
  }

  @GetMapping("/info")
  public ResponseEntity<?> getOrderById(@RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(id));
  }

  @GetMapping("/checked")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> getOrderByStatusChecked(){
    return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrderByStatusChecked());
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> deleteOrder(@RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(id));
  }

  @PutMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> updateOrder(@RequestBody @Valid OrderRequest orderRequest, @RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.editOrder(orderRequest, id));
  }

  @PutMapping("/status")
  public ResponseEntity<?> updateStatus(@RequestParam("status") String status, @RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.updateStatus(id, status));
  }


}
