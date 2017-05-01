package com.jakduk.api.configuration;

import com.jakduk.api.ApiApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */
public class ApiPropertiesTest extends ApiApplicationTests {

    @Autowired
    private ApiProperties sut;

    @Value("${api.rememberme.expiration}")
    private String apiRemembermeExpiration;

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Test
    public void getProperties() {
        System.out.println("phjang=" + sut.getRemembermeExpiration());
        System.out.println("phjang=" + sut.getPathUserPictureUrl());
        System.out.println("phjang=" + apiRemembermeExpiration);
        System.out.println("phjang=" + apiServerUrl);
    }
}
