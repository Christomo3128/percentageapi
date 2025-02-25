package com.percentageapi.infrastructure.controller;

import com.percentageapi.application.ports.input.CalculationService;
import com.percentageapi.domain.model.CalculationResult;
import com.percentageapi.infrastructure.repository.database.model.CustomPage;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/calculation")
public class PercentageController {

  private final CalculationService calculationService;

  public PercentageController(CalculationService calculationService) {
    this.calculationService = calculationService;
  }

  @GetMapping
  public ResponseEntity<CalculationResult> calculate(
      @RequestParam double num1,
      @RequestParam double num2) {
    CalculationResult result = calculationService.calculate(num1, num2);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/call-history")
  public ResponseEntity<CustomPage<RequestLogDTO>> LogHistory(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {
      Pageable pageable = PageRequest.of(page,size);
    CustomPage<RequestLogDTO> history = calculationService.getAllLogHistories(pageable);
    return ResponseEntity.ok(history);
  }
}
