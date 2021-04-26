package com.tusur.cargo.controller.advice;

import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

  @ExceptionHandler(SpringCargoException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleNotFoundException(SpringCargoException ex) {
    LOGGER.debug("handling 404 error");
    LOGGER.debug(ex.getMessage());
  }

  @ExceptionHandler(PasswordException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleBadRequestException(PasswordException ex) {
    LOGGER.debug("handling 400 error");
    LOGGER.debug(ex.getMessage());
  }

  @ExceptionHandler(UserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleBadRequestException(UserException ex) {
    LOGGER.debug("handling 400 error");
    LOGGER.debug(ex.getMessage());
  }
}
