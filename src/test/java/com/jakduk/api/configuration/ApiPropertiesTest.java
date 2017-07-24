package com.jakduk.api.configuration;

import com.jakduk.api.ApiApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */
public class ApiPropertiesTest extends ApiApplicationTests {

    @Autowired
    private ApiProperties sut;

    @Test
    public void getProperties() {
        Assert.assertTrue(sut.getUrlPath().getGalleryImage().equals("gallery"));
    }
}
