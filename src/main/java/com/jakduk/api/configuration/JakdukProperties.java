package com.jakduk.api.configuration;

import com.jakduk.api.configuration.rabbitmq.RabbitMQ;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */

@Configuration
@ConfigurationProperties("jakduk")
public class JakdukProperties {

    private String rememberMeSeed;
    private Integer rememberMeExpiration;
    private String apiServerUrl;
    private String webServerUrl;

    private ApiUrlPath apiUrlPath = new ApiUrlPath();
    private Rabbitmq rabbitmq = new Rabbitmq();

    public String getRememberMeSeed() {
        return rememberMeSeed;
    }

    public void setRememberMeSeed(String rememberMeSeed) {
        this.rememberMeSeed = rememberMeSeed;
    }

    public Integer getRememberMeExpiration() {
        return rememberMeExpiration;
    }

    public void setRememberMeExpiration(Integer rememberMeExpiration) {
        this.rememberMeExpiration = rememberMeExpiration;
    }

    public String getApiServerUrl() {
        return apiServerUrl;
    }

    public void setApiServerUrl(String apiServerUrl) {
        this.apiServerUrl = apiServerUrl;
    }

    public String getWebServerUrl() {
        return webServerUrl;
    }

    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }

    public ApiUrlPath getApiUrlPath() {
        return apiUrlPath;
    }

    public void setApiUrlPath(ApiUrlPath apiUrlPath) {
        this.apiUrlPath = apiUrlPath;
    }

    public Rabbitmq getRabbitmq() {
        return rabbitmq;
    }

    public void setRabbitmq(Rabbitmq rabbitmq) {
        this.rabbitmq = rabbitmq;
    }

    @Configuration
    @ConfigurationProperties("jakduk.api-url-path")
    public class ApiUrlPath {
        private String userPictureLarge;
        private String userPictureSmall;
        private String galleryImage;
        private String galleryThumbnail;

        public String getUserPictureLarge() {
            return userPictureLarge;
        }

        public void setUserPictureLarge(String userPictureLarge) {
            this.userPictureLarge = userPictureLarge;
        }

        public String getUserPictureSmall() {
            return userPictureSmall;
        }

        public void setUserPictureSmall(String userPictureSmall) {
            this.userPictureSmall = userPictureSmall;
        }

        public String getGalleryImage() {
            return galleryImage;
        }

        public void setGalleryImage(String galleryImage) {
            this.galleryImage = galleryImage;
        }

        public String getGalleryThumbnail() {
            return galleryThumbnail;
        }

        public void setGalleryThumbnail(String galleryThumbnail) {
            this.galleryThumbnail = galleryThumbnail;
        }
    }

    @Configuration
    @ConfigurationProperties("jakduk.mongodb")
    public class Mongodb {
        private String database;
        private String host;

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    @Configuration
    @ConfigurationProperties("jakduk.rabbitmq")
    public class Rabbitmq {
        private String exchangeName;
        private Map<String, RabbitMQ> queues = new HashMap<>();
        private Map<String, String> routingKeys = new HashMap<>();

        public String getExchangeName() {
            return exchangeName;
        }

        public void setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
        }

        public Map<String, RabbitMQ> getQueues() {
            return queues;
        }

        public void setQueues(Map<String, RabbitMQ> queues) {
            this.queues = queues;
        }

        public Map<String, String> getRoutingKeys() {
            return routingKeys;
        }

        public void setRoutingKeys(Map<String, String> routingKeys) {
            this.routingKeys = routingKeys;
        }
    }

    @Configuration
    @ConfigurationProperties("jakduk.elasticsearch")
    @Validated
    public class Elasticsearch {
        private Boolean enable;
        @NotEmpty private List<String> hostAndPort;
        private String indexBoard;
        private String indexGallery;
        private String indexSearchWord;
        private Integer bulkActions;
        private Integer bulkConcurrentRequests;
        private Integer bulkFlushIntervalSeconds;
        private Integer bulkSizeMb;

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public List<String> getHostAndPort() {
            return hostAndPort;
        }

        public void setHostAndPort(List<String> hostAndPort) {
            this.hostAndPort = hostAndPort;
        }

        public String getIndexBoard() {
            return indexBoard;
        }

        public void setIndexBoard(String indexBoard) {
            this.indexBoard = indexBoard;
        }

        public String getIndexGallery() {
            return indexGallery;
        }

        public void setIndexGallery(String indexGallery) {
            this.indexGallery = indexGallery;
        }

        public String getIndexSearchWord() {
            return indexSearchWord;
        }

        public void setIndexSearchWord(String indexSearchWord) {
            this.indexSearchWord = indexSearchWord;
        }

        public Integer getBulkActions() {
            return bulkActions;
        }

        public void setBulkActions(Integer bulkActions) {
            this.bulkActions = bulkActions;
        }

        public Integer getBulkConcurrentRequests() {
            return bulkConcurrentRequests;
        }

        public void setBulkConcurrentRequests(Integer bulkConcurrentRequests) {
            this.bulkConcurrentRequests = bulkConcurrentRequests;
        }

        public Integer getBulkFlushIntervalSeconds() {
            return bulkFlushIntervalSeconds;
        }

        public void setBulkFlushIntervalSeconds(Integer bulkFlushIntervalSeconds) {
            this.bulkFlushIntervalSeconds = bulkFlushIntervalSeconds;
        }

        public Integer getBulkSizeMb() {
            return bulkSizeMb;
        }

        public void setBulkSizeMb(Integer bulkSizeMb) {
            this.bulkSizeMb = bulkSizeMb;
        }
    }

    @Configuration
    @ConfigurationProperties("jakduk.storage")
    public class Storage {
        private String imagePath;
        private String thumbnailPath;
        private String userPictureLargePath;
        private String userPictureSmallPath;

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getThumbnailPath() {
            return thumbnailPath;
        }

        public void setThumbnailPath(String thumbnailPath) {
            this.thumbnailPath = thumbnailPath;
        }

        public String getUserPictureLargePath() {
            return userPictureLargePath;
        }

        public void setUserPictureLargePath(String userPictureLargePath) {
            this.userPictureLargePath = userPictureLargePath;
        }

        public String getUserPictureSmallPath() {
            return userPictureSmallPath;
        }

        public void setUserPictureSmallPath(String userPictureSmallPath) {
            this.userPictureSmallPath = userPictureSmallPath;
        }
    }

}
