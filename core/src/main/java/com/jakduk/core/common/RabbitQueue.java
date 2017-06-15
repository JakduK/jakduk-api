package com.jakduk.core.common;

import com.jakduk.core.configuration.CoreProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Jang,Pyohwan on 2017. 6. 15..
 */

@Getter
@Component
public enum  RabbitQueue {

    EMAIL;

    private String routingKey;

    @Resource
    private CoreProperties coreProperties;

    RabbitQueue() {
        this.routingKey = coreProperties.getRabbitmq().getQueues().get(0).getRoutingKey();
    }

}
