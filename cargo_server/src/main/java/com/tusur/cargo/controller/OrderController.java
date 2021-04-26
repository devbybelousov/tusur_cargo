package com.tusur.cargo.controller;

import com.tusur.cargo.dto.OrderPagingResponse;
import com.tusur.cargo.dto.OrderRequest;
import com.tusur.cargo.dto.PagingHeaders;
import com.tusur.cargo.enumiration.OrderStatus;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.service.OrderService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

  @GetMapping
  public ResponseEntity<?> getAllOrder(@And({
      @Spec(path = "title", params = "title", spec = Like.class),
      @Spec(path = "addressSender", params = "addressSender", spec = Like.class),
      @Spec(path = "addressRecipient", params = "addressRecipient", spec = In.class),
      @Spec(path = "type", params = "type", spec = Equal.class),
      @Spec(path = "price", params = "price", spec = Equal.class),
      @Spec(path = "departDate", params = "departDate", spec = Equal.class),
      @Spec(path = "arrivalDate", params = "arrivalDate", spec = Equal.class),
      @Spec(path = "created", params = "created", spec = Equal.class),
      @Spec(path = "size.width", params = "width", spec = Equal.class),
      @Spec(path = "size.height", params = "height", spec = Equal.class),
      @Spec(path = "size.length", params = "length", spec = Equal.class),
      @Spec(path = "size.weight", params = "weight", spec = Equal.class),
      @Spec(path = "status", params = "status", spec = Equal.class)})
      Specification<Order> spec,
      Sort sort,
      @RequestHeader HttpHeaders httpHeaders) {
    final OrderPagingResponse response = orderService.getAllOrder(spec, httpHeaders, sort);
    return ResponseEntity.status(HttpStatus.OK).headers(returnHttpHeaders(response))
        .body(response.getElements());
  }

  public HttpHeaders returnHttpHeaders(OrderPagingResponse response) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
    headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
    headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
    headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
    headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
    return headers;
  }

  @GetMapping("/info")
  public ResponseEntity<?> getOrderById(@RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(id));
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> deleteOrder(@RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(id));
  }

  @PutMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> updateOrder(@RequestBody @Valid OrderRequest orderRequest,
      @RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.editOrder(orderRequest, id));
  }

  @GetMapping("/accept")
  @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> acceptOrder(@RequestParam Long id){
    return ResponseEntity.status(HttpStatus.OK).body(orderService.changeStatusOrder(id, OrderStatus.ACTIVE.toString()));
  }

  @GetMapping("/reject")
  @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> rejectOrder(@RequestParam Long id){
    return ResponseEntity.status(HttpStatus.OK).body(orderService.changeStatusOrder(id, OrderStatus.REFUSE
        .toString()));
  }

  @GetMapping("/complete")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> completeStatus(@RequestParam("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(orderService.completeOrder(id));
  }


}
