package com.jakduk.core.common.rabbitmq;

import lombok.Getter;

/**
 * Created by Jang,Pyohwan on 2017. 6. 15..
 */

@Getter
public enum RabbitMQRoutingKey {

    EMAIL_WELCOME("email-welcome"),
    EMAIL_RESET_PASSWORD("email-rest-password");

    private String routingKey;

    RabbitMQRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

}
