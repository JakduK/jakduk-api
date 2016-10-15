package com.jakduk.batch;

import com.jakduk.core.CoreApplication;
import com.jakduk.core.configuration.CoreRootConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableBatchProcessing
@Import(CoreApplication.class)
public class JakdukBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(JakdukBatchApplication.class, args);
	}

}
