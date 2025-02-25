package com.percentageapi.application.ports.output;

import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestLogService {
  void saveLogRequest(RequestLogDTO requestLogDTO);

  Page<RequestLogDTO> getAllLogHistories(Pageable pageable);
}
