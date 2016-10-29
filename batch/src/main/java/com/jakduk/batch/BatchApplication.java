package com.jakduk.batch;

import com.jakduk.core.CoreApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableBatchProcessing
public class BatchApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.parent(CoreApplication.class)
				.child(BatchApplication.class)
				.web(false)
				.run(args);
	}

}
