package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.CoreConst;
import com.jakduk.api.common.util.AuthUtils;

import com.jakduk.api.common.util.CoreUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Resource
    private AuthUtils authUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (ObjectUtils.isEmpty(username)) {
            throw new IllegalArgumentException("email 은 꼭 필요한 값입니다.");
        } else {
            User user = userRepository.findOneByEmail(username)
                    .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ACCOUNT,
                            CoreUtils.getExceptionMessage("exception.not.found.jakduk.account", username)));

            UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user.getEmail(), user.getId(),
                    user.getPassword(), user.getUsername(), user.getProviderId(),
                    true, true, true, true, AuthUtils.getAuthorities(user.getRoles()));

            UserPicture userPicture = user.getUserPicture();

            if (! ObjectUtils.isEmpty(userPicture)) {
                UserPictureInfo userPictureInfo = new UserPictureInfo(userPicture,
                        authUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
                        authUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

                userDetailsImpl.setPicture(userPictureInfo);
            }

            log.info("login user:{}", userDetailsImpl.getUsername());

            return userDetailsImpl;
        }
    }
}
