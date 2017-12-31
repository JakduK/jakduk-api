package com.jakduk.api.common.rabbitmq;

import lombok.Getter;

/**
 * Created by Jang,Pyohwan on 2017. 6. 15..
 */

@Getter
public enum EmailRoutingKey {

    EMAIL_WELCOME("email-welcome"),
    EMAIL_RESET_PASSWORD("email-reset-password");

    private String routingKey;

    EmailRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

}
