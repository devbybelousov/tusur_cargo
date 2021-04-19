package com.tusur.cargo.controller;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.AdminResponse;
import com.tusur.cargo.enumiration.OrderStatus;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.service.AdminService;
import com.tusur.cargo.service.UserService;
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
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
@AllArgsConstructor
public class AdminController {

  private final AdminService adminService;
  private final UserService userService;

  @PostMapping
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> createAdmin(@RequestBody @Valid AdminRequest adminRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createAdmin(adminRequest));
  }

  @GetMapping
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> getAllAdmin(@And({
      @Spec(path = "email", params = "email", spec = Like.class),
      @Spec(path = "role.title", params = "role", spec = Like.class)})
      Specification<User> spec,
      Sort sort) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.getAllUser(spec, sort).stream()
        .map(user -> new AdminResponse(
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            user.getCountAccept(),
            user.getCountRefused(),
            user.getCountAccept() + user.getCountRefused()))
        .collect(Collectors.toList()));
  }
}
