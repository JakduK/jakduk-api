package com.jakduk.api.configuration;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private Integer rememberMeExpiration;
    private String apiServerUrl;
    private String webServerUrl;

    private UrlPath urlPath = new UrlPath();
    private Swagger swagger = new Swagger();
    private SlackLog slackLog = new SlackLog();

    @Getter
    @Setter
    public static class UrlPath {
        private String userPictureLarge;
        private String userPictureSmall;
        private String galleryImage;
        private String galleryThumbnail;
        private String boardFree;
    }

    @Getter
    @Setter
    public static class Swagger {
        private String protocol;
        private String host;
    }

    @Getter
    @Setter
    public static class SlackLog {
        private Boolean enabled;
        private Level level;
        private String webhook;
        private String channel;
        private String username;
    }

}
