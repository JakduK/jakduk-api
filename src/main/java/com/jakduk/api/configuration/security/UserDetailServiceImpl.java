package com.jakduk.api.configuration.security;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.repository.user.UserRepository;
import com.jakduk.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * Created by pyohwanjang on 2017. 4. 30..
 */

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource private AuthUtils authUtils;

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (ObjectUtils.isEmpty(username)) {
            throw new IllegalArgumentException("email 은 꼭 필요한 값입니다.");
        } else {
            User user = userRepository.findOneByEmail(username)
                    .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
                            JakdukUtils.getMessageSource("exception.not.found.jakduk.account", username)));

            String userId = user.getId();

            UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user.getEmail(), userId,
                    user.getPassword(), user.getUsername(), user.getProviderId(),
                    true, true, true, true, AuthUtils.getAuthorities(user.getRoles()));

            UserPicture userPicture = user.getUserPicture();

            if (! ObjectUtils.isEmpty(userPicture)) {
                UserPictureInfo userPictureInfo = new UserPictureInfo(userPicture.getId(),
                        authUtils.generateUserPictureUrl(Constants.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
                        authUtils.generateUserPictureUrl(Constants.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

                userDetailsImpl.setPicture(userPictureInfo);
            }

            log.info("login user:{}", userDetailsImpl.getUsername());

            userService.updateLastLogged(userId);

            return userDetailsImpl;
        }
    }
}
