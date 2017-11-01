package com.jakduk.api.configuration;

import ch.qos.logback.classic.Level;
import com.jakduk.api.configuration.rabbitmq.RabbitMQ;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties("jakduk")
public class JakdukProperties {

    private String rememberMeSeed;
    private Integer rememberMeExpiration;
    private String apiServerUrl;
    private String webServerUrl;

    private ApiUrlPath apiUrlPath = new ApiUrlPath();
    private Mongodb mongodb = new Mongodb();
    private Rabbitmq rabbitmq = new Rabbitmq();
    private Elasticsearch elasticsearch = new Elasticsearch();
    private Storage storage = new Storage();

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("jakduk.api-url-path")
    public class ApiUrlPath {
        private String userPictureLarge;
        private String userPictureSmall;
        private String galleryImage;
        private String galleryThumbnail;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("jakduk.mongodb")
    public class Mongodb {
        private String database;
        private String host;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("jakduk.rabbitmq")
    public class Rabbitmq {
        private String exchangeName;
        private Map<String, RabbitMQ> queues = new HashMap<>();
        private Map<String, String> routingKeys = new HashMap<>();
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("jakduk.elasticsearch")
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

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("jakduk.slack-log")
    public class SlackLog {
        private Boolean enabled;
        private Level level;
        private String webhook;
        private String channel;
        private String username;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("jakduk.storage")
    public class Storage {
        private String imagePath;
        private String thumbnailPath;
        private String userPictureLargePath;
        private String userPictureSmallPath;
    }

}
