package com.percentageapi.application.service;

import com.percentageapi.application.ports.input.CalculationService;
import com.percentageapi.domain.model.CalculationResult;
import com.percentageapi.infrastructure.config.RateLimiter;
import com.percentageapi.infrastructure.exception.ExternalServiceUnavailableException;
import com.percentageapi.infrastructure.exception.InternalServerErrorException;
import com.percentageapi.infrastructure.exception.RateLimitExceededException;
import com.percentageapi.infrastructure.repository.database.LogAdapter;
import com.percentageapi.infrastructure.repository.database.model.CustomPage;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import com.percentageapi.infrastructure.repository.restClient.PercentageAdapter;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CalculationServiceImplTest {

  @InjectMocks
  private CalculationService calculationService; // Cambia esto por el nombre real de tu clase que tiene el método calculate.

//  @Mock
  private RateLimiter rateLimiter; // Cambia esto por el tipo real de rateLimiter.

//  @Mock
  private PercentageAdapter percentageAdapter; // Cambia esto por el tipo real de percentageAdapter.

//  @Mock
  private LogAdapter logAdapter; // Cambia esto por el tipo real de logAdapter.

  @Mock
  private Page<RequestLogDTO> requestLogDTOPage; // Simulación de la página de registros.

  @BeforeEach
  void setUp() {
    rateLimiter = mock(RateLimiter.class);
    percentageAdapter = mock(PercentageAdapter.class);
    logAdapter = mock(LogAdapter.class);

    calculationService = new CalculationServiceImpl(percentageAdapter, rateLimiter, logAdapter);
  }

  @Test
  void shouldThrowRateLimitExceededExceptionWhenLimitExceeded() {
    when(rateLimiter.tryConsume()).thenReturn(false); // Simula que se ha excedido el límite.

    assertThrows(RateLimitExceededException.class, () -> {
      calculationService.calculate(10, 20); // Llama al método para calcular.
    });

    // Verifica que el método de registro fue llamado.
    verify(logAdapter).saveLogRequest(any(RequestLogDTO.class));
  }

  @Test
  void shouldCalculateResultSuccessfully() {
    when(rateLimiter.tryConsume()).thenReturn(true);
    when(percentageAdapter.getPercentage()).thenReturn(10.0);

    CalculationResult result = calculationService.calculate(100, 200);

    assertEquals(330.0, result.getResult());

    // Verifica que el objeto RequestLogDTO tiene los valores esperados
    RequestLogDTO expectedLog = new RequestLogDTO(
        "/api/calculate",
        "num1=100 num2=200",
        String.valueOf(result.getResult())
    );

    verify(logAdapter).saveLogRequest(any(RequestLogDTO.class));
  }

  @Test
  void shouldThrowExternalServiceUnavailableExceptionWhenPercentageServiceFails() {
    when(rateLimiter.tryConsume()).thenReturn(true);
    RetryableException retryableException = mock(RetryableException.class);
    // Configurar el comportamiento del mock para lanzar la excepción esperada
    when(retryableException.getMessage()).thenReturn("Service unavailable");
    when(percentageAdapter.getPercentage()).thenThrow(retryableException);

    // Ejecuta el método y verifica que se lanza la excepción correcta
    ExternalServiceUnavailableException exception = assertThrows(ExternalServiceUnavailableException.class, () -> {
      calculationService.calculate(10, 20);
    });

    // Verifica que el mensaje de la excepción sea el esperado
    assertEquals("External service is unavailable", exception.getMessage());

    // Verificar que el log se haya registrado correctamente
    verify(logAdapter).saveLogRequest(argThat(logRequestDTO ->
        logRequestDTO.getEndpoint().equals("/api/calculate") &&
            logRequestDTO.getParameters().equals("num1=10.0 num2=20.0") &&
            logRequestDTO.getResponse().equals("External service is unavailable")
    ));
  }

  @Test
  void shouldThrowInternalServerErrorExceptionOnOtherExceptions() {
    // Configura el mock para simular que el límite de tasa no ha sido excedido.
    when(rateLimiter.tryConsume()).thenReturn(true);
    when(percentageAdapter.getPercentage()).thenThrow(new RuntimeException("Unexpected error"));

    // Verifica que se lance la excepción y el mensaje es el esperado.
    InternalServerErrorException thrown = assertThrows(InternalServerErrorException.class,
        () -> calculationService.calculate(10, 20));
    assertEquals("Error calculating result", thrown.getMessage());

    // Captura el argumento pasado al método saveLogRequest
    ArgumentCaptor<RequestLogDTO> captor = ArgumentCaptor.forClass(RequestLogDTO.class);
    verify(logAdapter).saveLogRequest(captor.capture());

    RequestLogDTO capturedRequestLog = captor.getValue();

    // Verifica los valores capturados
    assertEquals("/api/calculate", capturedRequestLog.getEndpoint());
    assertEquals("num1=10.0 num2=20.0", capturedRequestLog.getParameters());
    assertEquals("Error calculating result", capturedRequestLog.getResponse());
  }

  @Test
  void shouldReturnAllLogHistories() {
    // Crea una lista de RequestLogDTO para simular el contenido de la página
    RequestLogDTO logEntry = new RequestLogDTO("/api/calculate", "num1=10 num2=20", "100");
    List<RequestLogDTO> logEntries = Collections.singletonList(logEntry);
    Page<RequestLogDTO> logPage = new PageImpl<>(logEntries);

    // Configura el mock para devolver la página simulada
    when(logAdapter.getAllLogHistories(any(Pageable.class))).thenReturn(logPage);

    // Realiza la llamada al método que deseas probar
    CustomPage<RequestLogDTO> result = calculationService.getAllLogHistories(Pageable.unpaged());

    // Verifica los resultados
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("/api/calculate", result.getContent().get(0).getEndpoint());
  }
}