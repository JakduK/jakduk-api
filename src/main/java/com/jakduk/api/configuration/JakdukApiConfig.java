package com.jakduk.api.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.jakduk.api.common.converter.DateToLocalDateTimeConverter;
import com.jakduk.api.common.converter.LocalDateTimeToDateConverter;

@Configuration
@EnableMongoRepositories("com.jakduk.api.repository")
public class JakdukApiConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	// yml로 뺄려고 했으나, Bean을 따로 등록 안해주면 thymeleaf에서 인식을 못하더라.
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setFallbackToSystemLocale(false);

		return messageSource;
	}

	@Bean
	public CustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(new DateToLocalDateTimeConverter());
		converters.add(new LocalDateTimeToDateConverter());
		return new MongoCustomConversions(converters);
	}

}
