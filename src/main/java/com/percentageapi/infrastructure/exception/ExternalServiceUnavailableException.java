package com.percentageapi.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
public
class ExternalServiceUnavailableException extends RuntimeException {
  public ExternalServiceUnavailableException(String message) {
    super(message);
  }
}