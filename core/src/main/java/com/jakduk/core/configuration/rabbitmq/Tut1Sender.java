package com.jakduk.core.configuration.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.stream.IntStream;

/**
 * Created by pyohwanjang on 2017. 6. 18..
 */

//@Configuration
public class Tut1Sender {

    @Autowired
    private RabbitTemplate template;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = "Hello World!";
        IntStream.range(0, 10)
                .forEach(action -> {
                    this.template.convertAndSend("jakduk-dev", "dev.email.welcome", message + action);
                    System.out.println(" [x] Sent '" + message + action + "'");
                });
    }
}
