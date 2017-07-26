package com.jakduk.api.configuration.rabbitmq;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jang,Pyohwan on 2017. 6. 14..
 */

@Getter
@Setter
public class RabbitMQ {

    private String bindingQueueName;
    private String bindingRoutingKey;
    private Boolean enabled;

}
