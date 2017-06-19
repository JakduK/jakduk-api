package com.jakduk.core.configuration.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.core.configuration.CoreProperties;
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
public class CoreRabbitMQConfig {

    @Resource
    private CoreProperties coreProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(coreProperties.getRabbitmq().getExchangeName());
    }

    @Bean
    public List<Binding> binding(TopicExchange exchange, List<Queue> queues) {
        Map<String, String> queueMap = coreProperties.getRabbitmq().getQueues().entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toMap(CoreRabbitMQ::getQueueName, CoreRabbitMQ::getRoutingKey));

        return queues.stream()
                .map(queue -> BindingBuilder.bind(queue).to(exchange).with(queueMap.get(queue.getName())))
                .collect(Collectors.toList());
    }

    @Bean
    public List<Queue> queues() {

        return coreProperties.getRabbitmq().getQueues().entrySet().stream()
                .map(queue -> {
                    CoreRabbitMQ coreRabbitMQ = queue.getValue();
                    return new Queue(coreRabbitMQ.getQueueName());
                })
                .collect(Collectors.toList());
    }

}
