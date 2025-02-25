package com.percentageapi.application.service;

import com.percentageapi.application.ports.input.CalculationService;
import com.percentageapi.domain.model.CalculationResult;
import com.percentageapi.infrastructure.config.RateLimiter;
import com.percentageapi.infrastructure.exception.ExternalServiceUnavailableException;
import com.percentageapi.infrastructure.exception.InternalServerErrorException;
import com.percentageapi.infrastructure.exception.NoCachedValueException;
import com.percentageapi.infrastructure.exception.RateLimitExceededException;
import com.percentageapi.infrastructure.repository.database.LogAdapter;
import com.percentageapi.infrastructure.repository.database.model.CustomPage;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import com.percentageapi.infrastructure.repository.restClient.PercentageAdapter;
import feign.RetryableException;
import io.lettuce.core.RedisConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CalculationServiceImpl implements CalculationService {

  private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);

  private final PercentageAdapter percentageAdapter;
  private final RateLimiter rateLimiter;
  private final LogAdapter logAdapter;

  public CalculationServiceImpl(PercentageAdapter percentageAdapter, RateLimiter rateLimiter, LogAdapter logAdapter) {
    this.percentageAdapter = percentageAdapter;
    this.rateLimiter = rateLimiter;
    this.logAdapter = logAdapter;
  }

  @Override
  public CalculationResult calculate(double num1, double num2) {
    validateRateLimit(num1, num2);
    CalculationResult result = null;
    try {
      double sum = num1 + num2;
      double percentage = percentageAdapter.getPercentage();
      result = new CalculationResult(sum + (sum * percentage / 100));
      this.logRequest(num1, num2, result, null);
    } catch (RetryableException e) {
      logger.error("ExternalServiceUnavailableException: {} ", e.getMessage());
      this.logRequest( num1, num2, null, "External service is unavailable");
      throw new ExternalServiceUnavailableException( "External service is unavailable");
    } catch (RedisConnectionException e) {
      logger.error("NoCachedValueException: {} ", e.getMessage());
      this.logRequest(num1, num2, null, "Unable to connect to Redis ");
      throw new NoCachedValueException("Unable to connect to Redis ");
    } catch (Exception e) {
      logger.error("InternalServerErrorException: {} ", e.getMessage());
      this.logRequest(num1, num2, null, "Error calculating result");
      throw new InternalServerErrorException("Error calculating result");
    }
    return result;
  }

  @Override
  public CustomPage<RequestLogDTO> getAllLogHistories(Pageable pageable) {
    Page<RequestLogDTO> requestLogDTOS = logAdapter.getAllLogHistories(pageable);
    return new CustomPage<>(requestLogDTOS);
  }

  private void validateRateLimit(double num1, double num2) {
    if (!rateLimiter.tryConsume()) {
      this.logRequest(num1, num2, null, "Rate limit exceeded");
      throw new RateLimitExceededException("Rate limit exceeded. Please try again later.");
    }
  }

  private void logRequest(double num1, double num2, CalculationResult calculationResult, String error) {
    String response = calculationResult != null ? String.valueOf(calculationResult.getResult()) : error;
    logAdapter.saveLogRequest(new RequestLogDTO("/api/calculate", "num1=" + num1 + " num2=" + num2, response));
  }
}
