package com.percentageapi.application.ports.input;

import com.percentageapi.domain.model.CalculationResult;
import com.percentageapi.infrastructure.repository.database.model.CustomPage;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import org.springframework.data.domain.Pageable;

public interface CalculationService {

  CalculationResult calculate(double num1, double num2);

  CustomPage<RequestLogDTO> getAllLogHistories(Pageable pageable);

}
