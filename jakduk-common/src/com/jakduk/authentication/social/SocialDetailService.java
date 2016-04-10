package com.jakduk.authentication.social;

import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.model.embedded.OAuthUser;
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
public class SocialDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OAuthUserOnLogin user = userRepository.findByOauthUser(CommonConst.OAUTH_TYPE_FACEBOOK, username);

        log.debug("user=" + user);

        OAuthPrincipal principal = new OAuthPrincipal(user.getId(), username, user.getUsername(), CommonConst.OAUTH_TYPE_FACEBOOK, user.getOauthUser().getAddInfoStatus(),
                true, true, true, true, getAuthorities(user.getRoles()));

        return principal;
    }


    public Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(roles));

        return authList;
    }

    public List<String> getRoles(List<Integer> roles) {
        List<String> newRoles = new ArrayList<String>();

        if (roles != null) {
            for (Integer roleNumber : roles) {
                String roleName = CommonRole.getRoleName(roleNumber);
                if (!roleName.isEmpty()) {
                    newRoles.add(roleName);
                }
            }
        }

        return newRoles;
    }

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
