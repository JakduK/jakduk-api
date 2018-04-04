package com.jakduk.api;

import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.security.UserDetailsImpl;
import com.jakduk.api.model.embedded.UserPictureInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

//@TestConfiguration
public class WithMockJakdukUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJakdukUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJakdukUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetailsImpl userDetails = new UserDetailsImpl("test07@test.com", "a!b@c#",
                "1234", "test07", Constants.ACCOUNT_TYPE.JAKDUK, true, true,
                true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        userDetails.setPicture(
                new UserPictureInfo(
                        "597a0d53807d710f57420aa5",
                        "https://dev-api.jakduk.com/user/picture/small/597a0d53807d710f57420aa5",
                        "https://dev-api.jakduk.com/user/picture/597a0d53807d710f57420aa5"
                )
        );

        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, "1234", userDetails.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
