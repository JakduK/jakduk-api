package com.jakduk.api.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class TestMvcConfig {

    @Bean
    public WithSecurityContextFactory securityContextFactory() {
        return new WithMockJakdukUserSecurityContextFactory();
    }
}
