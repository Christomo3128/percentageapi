package com.percentageapi.integration;

import com.percentageapi.application.ports.input.CalculationService;
import com.percentageapi.domain.model.CalculationResult;
import com.percentageapi.infrastructure.repository.database.model.CustomPage;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PercentageControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private CalculationService calculationService;

  private List<RequestLogDTO> requestLogDTOList;

  @BeforeEach
  void setUp() {
    requestLogDTOList = new ArrayList<>();
    requestLogDTOList.add(new RequestLogDTO("api/v1/call-history", "example data","{ \"result\": 11.0\n" + "}"));
  }

  @Test
  void calculateShouldReturnCalculationResult() throws Exception {
    // Dado
    double num1 = 10.0;
    double num2 = 20.0;
    CalculationResult calculationResult = new CalculationResult(33.0); // Suponiendo que el resultado es 30.0
    when(calculationService.calculate(num1, num2)).thenReturn(calculationResult);

    // Cuando y Entonces
    mockMvc.perform(get("/api/v1/calculation")
            .param("num1", String.valueOf(num1))
            .param("num2", String.valueOf(num2))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.result").value(33.0)); // Asegúrate de que el campo se ajuste a tu DTO
  }

  @Test
  void logHistoryShouldReturnLogHistory() throws Exception {
    // Crear un objeto Page
    Pageable pageable = PageRequest.of(0, 10);
    Page<RequestLogDTO> page = new PageImpl<>(requestLogDTOList, pageable, requestLogDTOList.size());
    CustomPage<RequestLogDTO> mockPage = new CustomPage<>(page);

    // Mock del servicio
    when(calculationService.getAllLogHistories(pageable)).thenReturn(mockPage);

    mockMvc.perform(get("/api/v1/calculation/call-history")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].endpoint").value("/api/calculate"))
        .andExpect(jsonPath("$.content[0].response").isNotEmpty());
  }

}
