package com.jakduk.batch;

import com.jakduk.core.CoreApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableBatchProcessing
public class BatchApplication {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(CoreApplication.class, BatchApplication.class);
		app.setWebEnvironment(false);
		app.run(args);
	}

}
