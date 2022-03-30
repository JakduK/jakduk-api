package com.jakduk.api.configuration.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.configuration.JakdukProperties;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by naver on 2017. 6. 2..
 */

@Configuration
public class RabbitMQConfig {

	@Resource
	private JakdukProperties.Rabbitmq rabbitmqProperties;

	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(rabbitmqProperties.getExchangeName());
	}

	@Bean
	public List<Binding> binding(TopicExchange exchange, List<Queue> queues) {
		Map<String, String> queueMap = rabbitmqProperties.getQueues().entrySet().stream()
			.map(Map.Entry::getValue)
			.collect(Collectors.toMap(RabbitMQ::getBindingQueueName, RabbitMQ::getBindingRoutingKey));

		return queues.stream()
			.map(queue -> BindingBuilder.bind(queue).to(exchange).with(queueMap.get(queue.getName())))
			.collect(Collectors.toList());
	}

	@Bean
	public List<Queue> queues() {

		return rabbitmqProperties.getQueues().entrySet().stream()
			.map(queue -> {
				RabbitMQ rabbitMQ = queue.getValue();
				return new Queue(rabbitMQ.getBindingQueueName());
			})
			.collect(Collectors.toList());
	}

}
