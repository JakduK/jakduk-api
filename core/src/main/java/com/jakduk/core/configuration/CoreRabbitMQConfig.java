package com.jakduk.core.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by naver on 2017. 6. 2..
 */

@Configuration
public class CoreRabbitMQConfig {

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
