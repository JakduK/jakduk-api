package com.jakduk.api;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class TestMvcConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder passwordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        passwordEncoder.setDefaultPasswordEncoderForMatches(new StandardPasswordEncoder());
        return passwordEncoder;
    }

}
