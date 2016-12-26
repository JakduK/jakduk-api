package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.SocialUserDetail;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.simple.UserOnAuthentication;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

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
			Optional<UserOnAuthentication> oUser = userRepository.findAuthUserByEmail(email);

            if (! oUser.isPresent())
                throw new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
                        CoreUtils.getExceptionMessage("exception.not.found.jakduk.account", email));

            UserOnAuthentication user = oUser.get();

            log.debug("user=" + user);

            return new SocialUserDetail(user.getId(), email, user.getUsername(), user.getProviderId(), user.getEmail(),
                    true, true, true, true, UserUtils.getAuthorities(user.getRoles()));
        }
    }
}
