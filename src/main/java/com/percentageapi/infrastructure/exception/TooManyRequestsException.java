package com.percentageapi.infrastructure.exception;

public class TooManyRequestsException extends RuntimeException {
  public TooManyRequestsException(String message) {
    super(message);
  }
}