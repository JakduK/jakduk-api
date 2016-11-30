package com.jakduk.api.configuration.authentication;

import com.jakduk.api.configuration.authentication.user.SocialUserDetail;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.model.simple.UserOnAuthentication;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 4. 8 오후 9:53
 */

@Slf4j
@Component
public class SocialDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("email 는 꼭 필요한 값입니다.");
        } else {
            UserOnAuthentication user = userRepository.findAuthUserByEmail(email);

            if (Objects.isNull(user))
                throw new UsernameNotFoundException("로그인 할 사용자 데이터가 존재하지 않습니다. email=" + email);

            log.debug("user=" + user);

            return new SocialUserDetail(user.getId(), email, user.getUsername(), user.getProviderId(), user.getEmail(),
                    true, true, true, true, getAuthorities(user.getRoles()));
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {

        return getGrantedAuthorities(getRoles(roles));
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
