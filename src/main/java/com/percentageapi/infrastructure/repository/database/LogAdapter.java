package com.percentageapi.infrastructure.repository.database;

import com.percentageapi.application.ports.output.RequestLogService;
import com.percentageapi.infrastructure.mapper.CallHistoryMapper;
import com.percentageapi.infrastructure.repository.database.model.RequestLog;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LogAdapter implements RequestLogService {

  private static final Logger logger = LoggerFactory.getLogger(LogAdapter.class);

  private final RequestLogRepository requestLogRepository;
  private final CallHistoryMapper callHistoryMapper;

  @Autowired
  public LogAdapter(RequestLogRepository requestLogRepository,
                    CallHistoryMapper callHistoryMapper) {
    this.requestLogRepository = requestLogRepository;
    this.callHistoryMapper = callHistoryMapper;
  }

  @Async("asyncExecutor")
  public void saveLogRequest(RequestLogDTO requestLogDTO) {
    try {
      logger.info("Guardar de registro de forma asincrónica - thread: {} ", Thread.currentThread().getName());
      requestLogRepository.save(callHistoryMapper.toEntity(requestLogDTO));
      logger.info("Log registrado correctamente");
    } catch (Exception e) {
      logger.error("Error registrando el log. No afecta la ejecución del endpoint. Detalles: {}", e.getMessage(), e);
    }

  }
  public Page<RequestLogDTO> getAllLogHistories(Pageable pageable) {
    Page<RequestLog> histories = requestLogRepository.findAll(pageable);
    return histories
        .map(CallHistoryMapper.INSTANCE::toDto);
  }
}
