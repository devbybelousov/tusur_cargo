package com.tusur.cargo.controller;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.PasswordRequest;
import com.tusur.cargo.dto.RecipientMessageRequest;
import com.tusur.cargo.service.UserService;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @GetMapping("/feedback")
  public ResponseEntity<?> getUserFeedback(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersFeedback(id));
  }

  @GetMapping("/recipient")
  public ResponseEntity<?> getUserRecipients(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersByCurrentUser(id));
  }

  @PostMapping("/recipient")
  public ResponseEntity<?> addUserRecipient(@RequestBody RecipientMessageRequest messageRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.createRecipientMessage(messageRequest));
  }

  @PostMapping("/admin")
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> createAdmin(@RequestBody @Valid AdminRequest adminRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAdmin(adminRequest));
  }

  @GetMapping("/ban")
  @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> banUser(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.banUser(id));
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> getAllUser() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
  }

  @PutMapping("/password")
  public ResponseEntity<?> editPassword(@RequestBody @Valid PasswordRequest passwordRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.editPassword(passwordRequest.getOldPassword(),
            passwordRequest.getNewPassword(), passwordRequest.getId()));
  }

  @PutMapping("/email")
  public ResponseEntity<?> editEmail(@RequestParam @Email @NotBlank @Size(max = 50) String email,
      @RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.editEmail(email, id));
  }

  @PutMapping("/email/{token}")
  public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.verifyEmail(token));
  }

  @PutMapping("/name")
  public ResponseEntity<?> editName(@RequestParam @NotBlank @Size(max = 30) String name,
      @RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.editName(name, id));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteUser(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
  }
}
