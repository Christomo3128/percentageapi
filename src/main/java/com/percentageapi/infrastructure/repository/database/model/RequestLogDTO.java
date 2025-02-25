package com.percentageapi.infrastructure.repository.database.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class RequestLogDTO {

  private LocalDateTime timestamp;
  private String endpoint;
  private String parameters;
  private String response;


  public RequestLogDTO(String endpoint, String parameters, String response) {
    this.endpoint = endpoint;
    this.parameters = parameters;
    this.response = response;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RequestLogDTO)) return false;
    RequestLogDTO that = (RequestLogDTO) o;
    return Objects.equals(endpoint, that.endpoint) &&
        Objects.equals(parameters, that.parameters) &&
        Objects.equals(response, that.response);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, parameters, response);
  }

}
