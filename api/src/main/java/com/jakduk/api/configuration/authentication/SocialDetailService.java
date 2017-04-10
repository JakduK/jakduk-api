package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.SocialUserDetails;
import com.jakduk.core.model.embedded.UserPictureInfo;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserPicture;
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

            log.debug("social user=" + user);

            SocialUserDetails socialUserDetails = new SocialUserDetails(user.getId(), email, user.getUsername(), user.getProviderId(), user.getEmail(),
                    true, true, true, true, UserUtils.getAuthorities(user.getRoles()));

            UserPicture userPicture = user.getUserPicture();

            if (! ObjectUtils.isEmpty(userPicture)) {
                UserPictureInfo userPictureInfo = new UserPictureInfo(userPicture,
                        userUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
                        userUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

                socialUserDetails.setPicture(userPictureInfo);
            }

            log.info("load Social username=" + socialUserDetails.getUsername());

            return socialUserDetails;
        }
    }
}
