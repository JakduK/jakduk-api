package com.jakduk.api;

import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.security.SnsAuthenticationProvider;
import com.jakduk.api.configuration.security.UserDetailServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties
@Import({JakdukProperties.class, UserDetailServiceImpl.class, AuthUtils.class})
public class TestMvcConfig {

    @MockBean
    private SnsAuthenticationProvider snsAuthenticationProvider;

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

}
