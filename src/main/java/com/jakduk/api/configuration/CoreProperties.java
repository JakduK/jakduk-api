package com.jakduk.api.configuration;

import com.jakduk.api.configuration.rabbitmq.CoreRabbitMQ;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jang,Pyohwan on 2017. 6. 12..
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "core")
public class CoreProperties {

    private Rabbitmq rabbitmq = new Rabbitmq();
    private Elasticsearch elasticsearch = new Elasticsearch();

    @Getter
    @Setter
    public class Rabbitmq {
        private String exchangeName;
        private Map<String, CoreRabbitMQ> queues = new HashMap<>();
        private Map<String, String> routingKeys = new HashMap<>();
    }

    @Getter
    @Setter
    public class Elasticsearch {
        private Boolean enable;
        private String indexBoard;
        private String indexGallery;
        private String indexSearchWord;
        private Integer bulkActions;
        private Integer bulkConcurrentRequests;
        private Integer bulkFlushIntervalSeconds;
        private Integer bulkSizeMb;
    }

}
