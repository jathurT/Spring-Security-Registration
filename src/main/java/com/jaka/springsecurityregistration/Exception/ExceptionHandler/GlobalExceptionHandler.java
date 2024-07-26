package com.jaka.SpringRegistration.Exception.ExceptionHandler;

import com.jaka.SpringRegistration.Exception.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({UserNotFoundException.class})
  public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(userNotFoundException.getMessage());
  }

  @ExceptionHandler({UserAlreadyExistException.class})
  public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistException exception) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(exception.getMessage());
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(exception.getMessage());
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(exception.getMessage());
  }

  @ExceptionHandler({DatabaseException.class})
  public ResponseEntity<?> handleDataAccessException(DatabaseException exception) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(exception.getMessage());
  }

  @ExceptionHandler({TokenNotFoundException.class})
  public ResponseEntity<?> handleTokenNotFoundException(TokenNotFoundException exception) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(exception.getMessage());
  }

  @ExceptionHandler({TokenExpiredException.class})
  public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException exception) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(exception.getMessage());
  }

  @ExceptionHandler({UserAlreadyVerifiedException.class})
  public ResponseEntity<?> handleUserAlreadyVerifiedException(UserAlreadyVerifiedException exception) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(exception.getMessage());
  }

  @ExceptionHandler({ArgumentNotFoundException.class})
  public ResponseEntity<?> handleArgumentNotFoundException(ArgumentNotFoundException exception) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(exception.getMessage());
  }

  @ExceptionHandler({UserNotVerifiedException.class})
  public ResponseEntity<?> handleUserNotVerifiedException(UserNotVerifiedException exception) {
    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(exception.getMessage());
  }



}

