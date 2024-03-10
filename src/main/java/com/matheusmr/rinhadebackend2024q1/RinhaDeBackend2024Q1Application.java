package com.matheusmr.rinhadebackend2024q1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RinhaDeBackend2024Q1Application {

	public static final String R2DBC_PROFILE = "r2dbc";
	public static final String IN_MEMORY_PROFILE = "in-memory";

	public static void main(String[] args) {
		SpringApplication.run(RinhaDeBackend2024Q1Application.class, args);
	}

}
