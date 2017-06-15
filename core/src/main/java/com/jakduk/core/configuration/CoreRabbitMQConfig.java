package com.jakduk.core.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(coreProperties.getRabbitmq().getExchangeName());
    }

    @Bean
    public List<Binding> binding(TopicExchange exchange, List<Queue> queues) {
        Map<String, String> queueMap = coreProperties.getRabbitmq().getQueues().stream()
                .collect(Collectors.toMap(RabbitmqQueue::getQueueName, RabbitmqQueue::getRoutingKey));

        return queues.stream()
                .map(queue -> BindingBuilder.bind(queue).to(exchange).with(queueMap.get(queue.getName())))
                .collect(Collectors.toList());
    }

    @Bean
    public List<Queue> queues() {
        return coreProperties.getRabbitmq().getQueues().stream()
                .map(queue -> new Queue(queue.getQueueName()))
                .collect(Collectors.toList());
    }

    @Bean
    public Tut1Receiver receiver() {
        return new Tut1Receiver();
    }

    @Bean
    public Tut1Sender sender() {
        return new Tut1Sender();
    }
}
