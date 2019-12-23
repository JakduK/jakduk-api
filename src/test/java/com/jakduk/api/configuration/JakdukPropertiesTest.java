package com.jakduk.api.configuration;

import com.jakduk.api.ApiApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */
public class JakdukPropertiesTest extends ApiApplicationTests {

    @Autowired
    private JakdukProperties sut;

    @Test
    public void getProperties() {
        assertTrue(sut.getApiUrlPath().getGalleryImage().equals("gallery"));
    }

    @Test
    public void rabbitmqTest() {
        assertFalse(sut.getRabbitmq().getQueues().isEmpty());
        assertFalse(sut.getRabbitmq().getRoutingKeys().isEmpty());
    }
}
