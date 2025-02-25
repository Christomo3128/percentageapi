package com.percentageapi.infrastructure.controller;

import com.percentageapi.application.ports.input.CalculationService;
import com.percentageapi.domain.model.CalculationResult;
import com.percentageapi.infrastructure.repository.database.model.CustomPage;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

class PercentageControllerTest {

  @InjectMocks
  private PercentageController percentageController;

  @Mock
  private CalculationService calculationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCalculate() {
    CalculationResult result = new CalculationResult(20.0);
    when(calculationService.calculate(anyDouble(), anyDouble())).thenReturn(result);

    ResponseEntity<CalculationResult> response = percentageController.calculate(10.0, 2.0);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(result, response.getBody());
  }

  @Test
  public void testLogHistory() {
    PageImpl<RequestLogDTO> page = new PageImpl<>(Collections.EMPTY_LIST);
    CustomPage<RequestLogDTO> customPage = new CustomPage<>(page);
    Pageable pageable = PageRequest.of(0, 10);
    when(calculationService.getAllLogHistories(pageable)).thenReturn(customPage);

    ResponseEntity<CustomPage<RequestLogDTO>> response = percentageController.LogHistory(0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(customPage, response.getBody());
  }
}