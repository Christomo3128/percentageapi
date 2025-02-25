package com.percentageapi.infrastructure.repository.restClient;

import com.percentageapi.infrastructure.repository.database.model.Percentage;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "percentageApi", url = "${percentage.api.url}")
public interface PercentageApi {
  @GetMapping(value = "/percentage", consumes = "application/json", produces = "application/json")
  @Headers("Content-Type: application/json")
  Percentage getPercentage();
}
