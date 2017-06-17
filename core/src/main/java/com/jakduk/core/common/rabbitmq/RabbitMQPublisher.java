package com.jakduk.core.common.rabbitmq;

import com.jakduk.core.configuration.CoreProperties;
import com.jakduk.core.model.rabbitmq.EmailPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

@Slf4j
@Component
public class RabbitMQPublisher {

    private final String QUEUE_EMAIL = "email";

    @Resource
    private CoreProperties coreProperties;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void emailPublish(String routingKey, EmailPayload message) {
        if (coreProperties.getRabbitmq().getQueues().get(QUEUE_EMAIL).getEnabled()) {
            rabbitTemplate.convertAndSend(coreProperties.getRabbitmq().getExchangeName(), routingKey, message);
        } else {
            log.info("Can not publish message. {} queue is disabled.", QUEUE_EMAIL);
        }

    }

}
