package com.percentageapi.domain.model;

public class CalculationResult {

  private double result;

  public CalculationResult(double result) {
    this.result = result;
  }

  public double getResult() {
    return result;
  }

  public void setResult(double result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "{" +
        "result:" + result +
        '}';
  }
}
