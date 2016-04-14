package com.jakduk.authentication.social;

import com.jakduk.common.CommonRole;
import com.jakduk.model.simple.SocialUserOnLogin;
import com.jakduk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pyohwan on 16. 4. 8.
 */

@Slf4j
public class SocialDetailService implements SocialUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public org.springframework.social.security.SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        log.debug("userId=" + userId);

        SocialUserOnLogin user = userRepository.findSocialUserByEmail(userId);

        log.debug("user=" + user);

        SocialUserDetail userDetail = new SocialUserDetail(user.getId(), user.getSocialInfo().getOauthId(), user.getUsername(), user.getSocialInfo().getProviderId(),
                true, true, true, true, getAuthorities(user.getRoles()));

        return userDetail;
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
