package com.tusur.cargo.controller;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.AdminResponse;
import com.tusur.cargo.enumeration.OrderStatus;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.service.AdminService;
import com.tusur.cargo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
@AllArgsConstructor
@Api(value = "admin", description = "API для операций администратора", tags = "Admin API")
public class AdminController {

  private final AdminService adminService;
  private final UserService userService;

  @PostMapping
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  @ApiOperation(value = "Добавить администратора")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Создан"),
      @ApiResponse(code = 400, message = "Данный email уже занят"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 404, message = "Роли 'ADMIN' нет в базе"),
      @ApiResponse(code = 403, message = "Доступ запрещен")
  })
  public ResponseEntity<?> createAdmin(@ApiParam("Информация об администраторе") @RequestBody @Valid AdminRequest adminRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(adminRequest));
  }

  @GetMapping
  @ApiOperation(value = "Получить всех администраторов")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен")
  })
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> getAllAdmin(@And({
      @Spec(path = "email", params = "email", spec = Like.class),
      @Spec(path = "role.title", params = "role", spec = Like.class)})
      Specification<User> spec,
      Sort sort) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser(spec, sort).stream()
        .map(user -> new AdminResponse(
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            countByStatus(user.getOrders(), OrderStatus.ACTIVE),
            countByStatus(user.getOrders(), OrderStatus.REFUSE)))
        .collect(Collectors.toList()));
  }

  private int countByStatus(List<Order> orders, OrderStatus status){
    int count = 0;
    for (Order order : orders){
      if (order.getStatus().equals(status)){
        count++;
      }
    }
    return count;
  }
}
