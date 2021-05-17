package com.tusur.cargo.controller;

import com.tusur.cargo.dto.InterlocutorRequest;
import com.tusur.cargo.dto.PasswordRequest;
import com.tusur.cargo.dto.UserResponse;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.model.User;
import com.tusur.cargo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
@Api(value = "user", description = "API для операций с пользователями", tags = "User API")
public class UserController {

  private final UserService userService;

  @ApiOperation(value = "Получить информацию о пользователе")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @GetMapping("/info")
  public ResponseEntity<?> getUserInfo(
      @ApiParam("Идентификатор пользователя") @RequestParam Long id) throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(id));
  }

  @ApiOperation(value = "Получить отзывы о пользователе")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @GetMapping("/feedback")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<?> getUserFeedback(
      @ApiParam("Идентификатор пользователя") @RequestParam Long id) throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersFeedback(id));
  }

  @ApiOperation(value = "Получить собеседников текущего пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @GetMapping("/recipient")
  @PreAuthorize("#id == authentication.principal.id")
  public ResponseEntity<?> getUserRecipients(
      @ApiParam("Идентификатор пользователя") @RequestParam Long id) throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllInterlocutorByUser(id));
  }

  @ApiOperation(value = "Добавить собеседника текущему пользователю")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @PostMapping("/recipient")
  @PreAuthorize("#messageRequest.userId == authentication.principal.id")
  public ResponseEntity<?> addUserRecipient(@RequestBody InterlocutorRequest messageRequest)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.addInterlocutor(messageRequest));
  }

  @ApiOperation(value = "Заблокировать пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @GetMapping("/ban")
  @PreAuthorize("hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> banUser(@ApiParam("Идентификатор пользователя") @RequestParam Long id)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(userService.banUser(id));
  }

  @ApiOperation(value = "Получить всех пользователей")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
  public ResponseEntity<?> getAllUser(@And({
      @Spec(path = "email", params = "email", spec = Like.class),
      @Spec(path = "name", params = "name", spec = Like.class),
      @Spec(path = "deleted", params = "deleted_at", spec = Equal.class),
      @Spec(path = "role.title", params = "role", spec = Like.class)})
      Specification<User> spec,
      Sort sort) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.getAllUser(spec, sort).stream().map(
            user -> new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getOrders() == null ? 0 : user.getOrders().size(),
                user.getFeedbackList() == null ? 0 : user.getFeedbackList().size(),
                user.getRating()))
            .collect(Collectors.toList()));
  }

  @ApiOperation(value = "Изменить пароль текущего пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 400, message = "Неверный пароль"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @PutMapping("/password")
  @PreAuthorize("#passwordRequest.id == authentication.principal.id")
  public ResponseEntity<?> editPassword(@RequestBody @Valid PasswordRequest passwordRequest)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.editPassword(passwordRequest.getOldPassword(),
            passwordRequest.getNewPassword(), passwordRequest.getId()));
  }

  @ApiOperation(value = "Изменить почту текущего пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @PutMapping("/email")
  @PreAuthorize("hasAuthority('USER') && #id == authentication.principal.id")
  public ResponseEntity<?> editEmail(
      @ApiParam("Почта пользователя") @RequestParam @Email @NotBlank @Size(max = 50) String email,
      @ApiParam("Идентификатор пользователя") @RequestParam Long id) throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.editEmail(email, id));
  }

  @ApiOperation(value = "Подтвердить смену почты пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 404, message = "Пользователь не найден или не верный токен")
  })
  @GetMapping("/email/{email}/{token}")
  public ResponseEntity<?> verifyEmail(
      @ApiParam("Токен для подтверждения почты") @PathVariable("token") String token,
      @ApiParam("Почта пользователя") @PathVariable("email") String email)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.verifyEmail(token, email));
  }

  @ApiOperation(value = "Изменить имя пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @PutMapping("/name")
  @PreAuthorize("hasAuthority('USER') && #id == authentication.principal.id")
  public ResponseEntity<?> editName(
      @ApiParam("Имя пользователя") @RequestParam @NotBlank @Size(max = 30) String name,
      @ApiParam("Идентификатор пользователя") @RequestParam Long id) throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.editName(name, id));
  }

  @ApiOperation(value = "Удалить текущего пользователя")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь не найден")
  })
  @DeleteMapping
  @PreAuthorize("hasAuthority('USER') && #id == authentication.principal.id")
  public ResponseEntity<?> deleteUser(@ApiParam("Идентификатор пользователя") @RequestParam Long id)
      throws NotFoundException {
    return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
  }
}
