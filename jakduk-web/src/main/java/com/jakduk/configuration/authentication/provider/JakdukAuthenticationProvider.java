package com.jakduk.configuration.authentication.provider;

import com.jakduk.configuration.authentication.JakdukDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author pyohwan
 *         16. 7. 31 오전 12:19
 */

@Component
public class JakdukAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JakdukDetailsService jakdukDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
