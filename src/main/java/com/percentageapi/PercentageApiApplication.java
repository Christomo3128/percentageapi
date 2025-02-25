package com.percentageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PercentageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PercentageApiApplication.class, args);
	}

}
