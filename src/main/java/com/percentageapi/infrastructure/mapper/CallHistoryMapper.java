package com.percentageapi.infrastructure.mapper;

import com.percentageapi.infrastructure.repository.database.model.RequestLog;
import com.percentageapi.infrastructure.repository.database.model.RequestLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CallHistoryMapper {
  CallHistoryMapper INSTANCE = Mappers.getMapper(CallHistoryMapper.class);

  RequestLogDTO toDto(RequestLog callHistory);

  @Mapping(target = "id", ignore = true)
  RequestLog toEntity(RequestLogDTO callHistoryDTO);
}
