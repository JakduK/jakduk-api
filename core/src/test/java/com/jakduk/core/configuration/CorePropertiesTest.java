package com.jakduk.core.configuration;

import com.jakduk.core.CoreApplicationTests;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */
public class CorePropertiesTest extends CoreApplicationTests {

    @Resource
    private CoreProperties sut;

    @Test
    public void rabbitmqTest() {
        Assert.assertFalse(sut.getRabbitmq().getQueues().isEmpty());
        Assert.assertFalse(sut.getRabbitmq().getRoutingKeys().isEmpty());
    }
}
