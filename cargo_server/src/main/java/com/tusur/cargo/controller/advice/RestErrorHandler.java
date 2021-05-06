package com.tusur.cargo.controller.advice;

import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class RestErrorHandler {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleNotFoundException(NotFoundException ex) {
    log.debug("handling 404 error");
    log.debug(ex.getMessage());
  }

  @ExceptionHandler(PasswordException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleBadRequestException(PasswordException ex) {
    log.debug("handling 400 error");
    log.debug(ex.getMessage());
  }

  @ExceptionHandler(UserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleBadRequestException(UserException ex) {
    log.debug("handling 400 error");
    log.debug(ex.getMessage());
  }
}
