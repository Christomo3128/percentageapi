package com.percentageapi.infrastructure.repository.restClient;

import com.percentageapi.application.ports.output.PercentageService;
import com.percentageapi.infrastructure.repository.redis.CacheAdapter;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class PercentageAdapter implements PercentageService {
  private static final Logger logger = LoggerFactory.getLogger(PercentageAdapter.class);

  private final PercentageApi percentageApi;
  private final CacheAdapter cacheAdapter;

  private int attemptCounter = 0;

  @Autowired
  public PercentageAdapter(PercentageApi percentageApi, CacheAdapter cacheAdapter) {
    this.percentageApi = percentageApi;
    this.cacheAdapter = cacheAdapter;
  }

  @Retryable(retryFor = {RetryableException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
  public double getPercentage() {
    Double percentage = cacheAdapter.getCachedPercentage();
    if (percentage != null) {
      return percentage;
    }

    // Si no está en caché, obtenemos el valor de la API externa con reintentos
    percentage = fetchPercentageFromApi();

    // Guardamos en caché solo si Redis está disponible (sin excepciones)
    cacheAdapter.cachePercentage(percentage);

    return percentage;
  }

  public double fetchPercentageFromApi() {
    attemptCounter++;
    logger.info("Intento de obtener porcentaje desde API externa: {}", attemptCounter);
    return percentageApi.getPercentage().getPayload().getAmount();
  }
}
