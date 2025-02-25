package com.percentageapi.infrastructure.repository.database.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class RequestLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime timestamp;
  private String endpoint;
  private String parameters;
  private String response;

  public RequestLog() {
  }

  public RequestLog(LocalDateTime timestamp, String endpoint, String parameters, String response) {
    this.timestamp = timestamp;
    this.endpoint = endpoint;
    this.parameters = parameters;
    this.response = response;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    if(timestamp == null) {
      this.timestamp = LocalDateTime.now();
    } else {
      this.timestamp = timestamp;
    }

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
}
