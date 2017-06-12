package com.jakduk.core.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

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

//    @Bean
//    public List<Binding> binding(TopicExchange exchange, Queue queue) {
//        return BindingBuilder.bind(queue).to(exchange).with("");
//    }

    @Bean
    public Queue hello() {
        return new Queue("hello");
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
