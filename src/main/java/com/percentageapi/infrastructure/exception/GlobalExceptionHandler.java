package com.percentageapi.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RateLimitExceededException.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleRateLimitExceededException(RateLimitExceededException ex) {
    return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
  }

  @ExceptionHandler(NoCachedValueException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleNoCachedValueException(NoCachedValueException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(ExternalServiceUnavailableException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleExternalServiceUnavailableException(ExternalServiceUnavailableException ex) {
    return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
  }

  @ExceptionHandler(InternalServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handleInternalServerErrorException(InternalServerErrorException ex) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", status.value());
    errorResponse.put("error", status.getReasonPhrase());
    errorResponse.put("message", message);
    return new ResponseEntity<>(errorResponse, status);
  }
}
