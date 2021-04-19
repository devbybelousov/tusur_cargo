package com.tusur.cargo.controller;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.PasswordRequest;
import com.tusur.cargo.dto.RecipientMessageRequest;
import com.tusur.cargo.dto.UserResponse;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.service.UserService;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

  @GetMapping("/ban")
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> banUser(@RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.banUser(id));
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> getAllUser(@And({
      @Spec(path = "email", params = "email", spec = Like.class),
      @Spec(path = "name", params = "name", spec = Like.class),
      @Spec(path = "role.title", params = "role", spec = Like.class)})
      Specification<User> spec,
      Sort sort) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser(spec, sort).stream().map(
        user -> new UserResponse(
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            user.getOrders().size(),
            user.getFeedbackList().size(),
            user.getRating()))
        .collect(Collectors.toList()));
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

  @GetMapping("/email/{email}/{token}")
  public ResponseEntity<?> verifyEmail(@PathVariable("token") String token, @PathVariable("email") String email) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.verifyEmail(token, email));
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
