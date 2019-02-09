package com.jakduk.api;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.security.SnsAuthenticationProvider;
import com.jakduk.api.configuration.security.UserDetailServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties
@Import({JakdukProperties.class, UserDetailServiceImpl.class})
public class TestMvcConfig {

    @MockBean
    private SnsAuthenticationProvider snsAuthenticationProvider;

}
