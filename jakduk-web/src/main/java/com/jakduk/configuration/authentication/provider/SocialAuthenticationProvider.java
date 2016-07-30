package com.jakduk.configuration.authentication.provider;

import com.jakduk.configuration.authentication.SocialDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author pyohwan
 *         16. 7. 30 오후 11:52
 */

@Component
public class SocialAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SocialDetailService socialDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
