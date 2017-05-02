package com.jakduk.api.configuration;

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
    private String serverUrl;
    private UrlPath urlPath = new UrlPath();

    @Getter
    @Setter
    public static class UrlPath {
        private String userPictureLarge;
        private String userPictureSmall;
        private String galleryImage;
        private String galleryThumbnail;
    }

}
