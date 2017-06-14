package com.jakduk.core.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jang,Pyohwan on 2017. 6. 14..
 */

@Getter
@Setter
public class RabbitmqQueue {
    private String queueName;
    private String routingKey;
}
