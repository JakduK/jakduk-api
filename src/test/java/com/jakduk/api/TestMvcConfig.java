package com.jakduk.api;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import java.util.List;

public class TestMvcConfig extends WebMvcConfigurerAdapter {

    // org.springframework.mobile.device.Device 구현체를 주입하기 위함
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()));
        super.addArgumentResolvers(argumentResolvers);
    }

}
