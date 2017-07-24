package com.jakduk.api.configuration.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by pyohwanjang on 2017. 4. 29..
 */
public class SnsAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    public SnsAuthenticationToken(Object principal) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal;
    }

    public SnsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
