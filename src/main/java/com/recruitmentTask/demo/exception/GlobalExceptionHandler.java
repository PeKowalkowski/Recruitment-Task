package com.recruitmentTask.demo.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> fieldErrors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    log.warn("Validation failed: {}", fieldErrors);

    ErrorResponse response = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Validation failed",
      fieldErrors,
      LocalDateTime.now()
    );

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
    log.error("Unexpected error", ex);

    ErrorResponse response = new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "Internal server error: " + ex.getMessage(),
      null,
      LocalDateTime.now()
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }

  public static class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, Map<String, String> errors, LocalDateTime timestamp) {
      this.status = status;
      this.message = message;
      this.errors = errors;
      this.timestamp = timestamp;
    }

    public int getStatus() {
      return status;
    }

    public String getMessage() {
      return message;
    }

    public Map<String, String> getErrors() {
      return errors;
    }

    public LocalDateTime getTimestamp() {
      return timestamp;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public void setErrors(Map<String, String> errors) {
      this.errors = errors;
    }

    public void setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
    }
  }
}
