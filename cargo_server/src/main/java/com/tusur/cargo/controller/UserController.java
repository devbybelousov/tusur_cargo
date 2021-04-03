package com.tusur.cargo.controller;

import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.service.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @GetMapping("/info")
  public ResponseEntity<?> getUserInfo(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(id));
  }

  @GetMapping("/ban")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> banUser(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.banUser(id));
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> getAllUser() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
  }

  @PutMapping
  public ResponseEntity<?> editUser(@RequestBody @Valid SignupRequest signupRequest,
      @RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.editUser(signupRequest, id));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteUser(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
  }
}
