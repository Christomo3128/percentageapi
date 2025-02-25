package com.percentageapi.application.ports.output;

import jakarta.servlet.UnavailableException;

public interface PercentageService {

  double getPercentage() throws UnavailableException;
}
