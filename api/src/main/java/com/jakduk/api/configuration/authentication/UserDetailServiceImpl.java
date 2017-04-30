package com.jakduk.api.configuration.authentication;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserPicture;
import com.jakduk.core.model.embedded.UserPictureInfo;
import com.jakduk.core.repository.user.UserRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Resource
    private UserUtils userUtils;

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
                    true, true, true, true, UserUtils.getAuthorities(user.getRoles()));

            UserPicture userPicture = user.getUserPicture();

            if (! ObjectUtils.isEmpty(userPicture)) {
                UserPictureInfo userPictureInfo = new UserPictureInfo(userPicture,
                        userUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
                        userUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

                userDetailsImpl.setPicture(userPictureInfo);
            }

            log.info("login user:{}", userDetailsImpl.getUsername());

            return userDetailsImpl;
        }
    }
}
