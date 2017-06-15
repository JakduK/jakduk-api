package com.jakduk.core.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.stream.IntStream;

/**
 * Created by naver on 2017. 6. 2..
 */
public class Tut1Sender {

    @Autowired
    private RabbitTemplate template;

//    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        String message = "Hello World!";
        IntStream.range(0, 5)
                .forEach(action -> {
                    this.template.convertAndSend("dev.email", message + action);
                    System.out.println(" [x] Sent '" + message + action + "'");
                });
    }
}
