package com.tusur.cargo.controller;

import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.service.AuthService;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;


  @PostMapping("/sign-up")
  public ResponseEntity<?> register(@RequestBody @Valid SignupRequest signupRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(signupRequest));
  }

  @GetMapping("/accountVerification/{token}")
  public ResponseEntity<?> verifyAccount(@PathVariable(name = "token") String token) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.verifyAccount(token));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(authService.login(loginRequest));
  }

  @GetMapping("/forgot")
  public ResponseEntity<?> forgotPassword(@RequestParam @Email @Size(max = 50) String email) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.forgotPassword(email));
  }

  @GetMapping("/recovery/{token}")
  public ResponseEntity<?> recovery(@RequestParam @Size(max = 12, min = 6) String password,
      @PathVariable(name = "token") String token) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.changePassword(password, token));
  }
}
