package com.percentageapi.infrastructure.repository.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CacheAdapter {

  private static final Logger logger = LoggerFactory.getLogger(CacheAdapter.class);
  private final CacheManager cacheManager;

  public CacheAdapter(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  public Double getCachedPercentage() {
    try {
      Cache cache = cacheManager.getCache("percentage");
      if (cache != null) {
        return Optional.ofNullable(cache.get("getPercentage", Double.class)).orElse(null);
      }
    } catch (Exception e) {
      logger.warn("⚠️ Error al obtener caché de Redis, continuando sin caché...");
    }
    return null;
  }

  public void cachePercentage(Double percentage) {
    try {
      Cache cache = cacheManager.getCache("percentage");
      if (cache != null) {
        cache.put("getPercentage", percentage);
      }
    } catch (Exception e) {
      logger.warn("⚠️ Redis no disponible, no se pudo almacenar en caché.");
    }
  }
}
