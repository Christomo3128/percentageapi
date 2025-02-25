package com.percentageapi.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiter {
  private final Bucket bucket;

  public RateLimiter() {
    Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1)));
    this.bucket = Bucket4j.builder().addLimit(limit).build();
  }

  public boolean tryConsume() {
    return bucket.tryConsume(1);
  }
}