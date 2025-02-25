package com.percentageapi.infrastructure.repository.database;

import com.percentageapi.infrastructure.repository.database.model.RequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
  Page<RequestLog> findAll(Pageable pageable);
}
