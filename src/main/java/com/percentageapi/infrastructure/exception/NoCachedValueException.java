package com.percentageapi.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoCachedValueException extends RuntimeException {
  public NoCachedValueException(String message) {
    super(message);
  }
}
