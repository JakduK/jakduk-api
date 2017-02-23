package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.SocialUserDetail;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserImage;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
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

    @Resource
    private UserUtils userUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("email 는 꼭 필요한 값입니다.");
        } else {
			User user = userRepository.findOneByEmail(email)
                    .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
                            CoreUtils.getExceptionMessage("exception.not.found.jakduk.account", email)));

            log.debug("user=" + user);

            String userImageId = null;
            UserImage userImage = user.getUserImage();

            if (! ObjectUtils.isEmpty(userImage))
                userImageId = userImage.getId();

            SocialUserDetail socialUserDetail = new SocialUserDetail(user.getId(), email, user.getUsername(), user.getProviderId(), user.getEmail(),
                    userUtils.generateUserImageUrl(userImageId),
                    true, true, true, true, UserUtils.getAuthorities(user.getRoles()));

            log.info("load Social username=" + socialUserDetail.getUsername());

            return socialUserDetail;
        }
    }
}
