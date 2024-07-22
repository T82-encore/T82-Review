package com.T82.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@SpringBootApplication
public class ReviewApplication {

	@Bean
	public RecordMessageConverter converter() {
		return new JsonMessageConverter();
	}

	public static void main(String[] args) {
		SpringApplication.run(ReviewApplication.class, args);
	}

}
