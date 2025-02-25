package com.percentageapi.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheErrorHandler implements CacheErrorHandler {
  private static final Logger logger = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

  @Override
  public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
    logger.warn("Error al obtener caché de Redis: {} - Clave: {}", exception.getMessage(), key);
  }

  @Override
  public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
    logger.warn("Error al guardar en caché en Redis: {} - Clave: {}", exception.getMessage(), key);
  }

  @Override
  public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
    logger.warn("Error al eliminar caché en Redis: {} - Clave: {}", exception.getMessage(), key);
  }

  @Override
  public void handleCacheClearError(RuntimeException exception, Cache cache) {
    logger.warn("Error al limpiar caché en Redis: {}", exception.getMessage());
  }
}
