package com.percentageapi.infrastructure.config;

import com.percentageapi.infrastructure.exception.RedisCacheErrorHandler;
import io.lettuce.core.ClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@EnableCaching(proxyTargetClass = true)
public class RedisConfig {
  private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Value("${spring.data.redis.password}")
  private String password;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    try {
      RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
      config.setHostName(host);
      config.setPort(port);
      config.setPassword(password);

      // Desactivar la reconexión automática de Lettuce
      LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
          .clientOptions(ClientOptions.builder().autoReconnect(false).build())
          .build();
      return new LettuceConnectionFactory(config, clientConfig);
    } catch (Exception e) {
      logger.error("⚠️ Redis no está disponible, ejecutando sin caché...");
      return null;
    }
  }


  @Bean
  public CacheErrorHandler errorHandler() {
    return new RedisCacheErrorHandler();
  }
}
