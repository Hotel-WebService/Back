package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class hoteljpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(hoteljpaApplication.class, args);
	}

}
