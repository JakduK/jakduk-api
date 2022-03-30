package com.jakduk.api;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.security.SnsAuthenticationProvider;

@Import({JakdukProperties.class})
@MockBeans({
    @MockBean(UserDetailsService.class), @MockBean(SnsAuthenticationProvider.class)
})
public class TestMvcConfig {
}
