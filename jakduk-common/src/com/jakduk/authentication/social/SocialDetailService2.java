package com.jakduk.authentication.social;

import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.model.simple.OAuthUserOnLogin;
import com.jakduk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pyohwan on 16. 4. 8.
 */

@Slf4j
public class SocialDetailService2 implements SocialUserDetailsService {

    private UserDetailsService userDetailsService;

    public SocialDetailService2(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        log.debug("phjang : {}", userId);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return (SocialUserDetails) userDetails;
    }

}
