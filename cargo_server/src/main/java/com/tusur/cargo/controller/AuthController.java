package com.tusur.cargo.controller;

import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/auth")
@AllArgsConstructor
@Api(value = "auth", description = "API для операций аутентификации", tags = "Auth API")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/sign-up")
  @ApiOperation(value = "Регистрация пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Создан"),
      @ApiResponse(code = 400, message = "Данный email уже занят"),
      @ApiResponse(code = 404, message = "Роли 'USER' нет в базе")
  })
  public ResponseEntity<?> register(
      @ApiParam("Информация о пользователе") @RequestBody @Valid SignupRequest signupRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(signupRequest));
  }

  @GetMapping("/accountVerification/{token}")
  @ApiOperation(value = "Подтверждение аккаунта")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 404, message = "Неверный токен")
  })
  public ResponseEntity<?> verifyAccount(
      @ApiParam("Токен для подтверждения аккаунта") @PathVariable(name = "token") String token)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(authService.verifyAccount(token));
  }

  @PostMapping("/sign-in")
  @ApiOperation(value = "Авторизация пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Неверный логин или пароль")
  })
  public ResponseEntity<?> loginUser(
      @ApiParam("Информация об авторизации") @RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
  }

  @GetMapping("/forgot")
  @ApiOperation(value = "Восстановление пароля")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  public ResponseEntity<?> forgotPassword(
      @ApiParam("Почта пользователя") @RequestParam @Email @Size(max = 50) String email) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.forgotPassword(email));
  }

  @GetMapping("/recovery")
  @ApiOperation(value = "Подтверждение смены пароля")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 404, message = "Пользователь не найден или не верный токен"),
      @ApiResponse(code = 400, message = "Неверный пароль")
  })
  public ResponseEntity<?> recovery(
      @ApiParam("Пароль пользователя") @RequestParam @Size(max = 12, min = 6) String password,
      @ApiParam("Токен для смены пароля") @RequestParam(name = "token") String token)
      throws PasswordException {
    return ResponseEntity.status(HttpStatus.OK).body(authService.changePassword(password, token));
  }
}
