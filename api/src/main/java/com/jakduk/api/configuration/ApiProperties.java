package com.jakduk.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */

@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private String remembermeExpiration;
    private String pathUserPictureUrl;

    public String getRemembermeExpiration() {
        return remembermeExpiration;
    }

    public void setRemembermeExpiration(String remembermeExpiration) {
        this.remembermeExpiration = remembermeExpiration;
    }

    public String getPathUserPictureUrl() {
        return pathUserPictureUrl;
    }

    public void setPathUserPictureUrl(String pathUserPictureUrl) {
        this.pathUserPictureUrl = pathUserPictureUrl;
    }
}
