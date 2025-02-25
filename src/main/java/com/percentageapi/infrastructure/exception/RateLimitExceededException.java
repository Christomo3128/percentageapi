package com.percentageapi.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException {
  public RateLimitExceededException(String message) {
    super(message);
  }

}
