package com.jakduk.core.common.rabbitmq;

import lombok.Getter;

/**
 * Created by Jang,Pyohwan on 2017. 6. 15..
 */

@Getter
public enum RabbitMQRoutingKey {

    EMAIL_WELCOME("email-welcome"),
    EMAIL_RESET_PASSWORD("email-reset-password"),
    ELASTICSEARCH_INDEX_DOCUMENT_BOARD("elasticsearch-index-document-board"),
    ELASTICSEARCH_DELETE_DOCUMENT_BOARD("elasticsearch-delete-document-board");

    private String routingKey;

    RabbitMQRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

}
