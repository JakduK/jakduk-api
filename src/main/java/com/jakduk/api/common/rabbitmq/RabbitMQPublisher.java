package com.jakduk.api.common.rabbitmq;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.rabbitmq.EmailPayload;
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
    private final String QUEUE_ELASTICSEARCH = "elasticsearch";

    @Resource private JakdukProperties.Rabbitmq rabbitmqProperties;

    @Autowired private RabbitTemplate rabbitTemplate;

    public void publishEmail(String routingKey, EmailPayload message) {
        if (rabbitmqProperties.getQueues().get(QUEUE_EMAIL).getEnabled()) {
            rabbitTemplate.convertAndSend(rabbitmqProperties.getExchangeName(), routingKey, message);
        } else {
            log.info("Can not publish message. {} queue is disabled.", QUEUE_EMAIL);
        }
    }

    public void publishElasticsearch(String routingKey, Object message) {
        if (rabbitmqProperties.getQueues().get(QUEUE_ELASTICSEARCH).getEnabled()) {
            rabbitTemplate.convertAndSend(rabbitmqProperties.getExchangeName(), routingKey, message);
        } else {
            log.info("Can not publish message. {} queue is disabled.", QUEUE_ELASTICSEARCH);
        }
    }

}
