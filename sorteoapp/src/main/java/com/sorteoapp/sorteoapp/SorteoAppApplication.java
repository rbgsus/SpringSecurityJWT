package com.sorteoapp.sorteoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SorteoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SorteoAppApplication.class, args);
	}
}
