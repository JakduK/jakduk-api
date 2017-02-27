package com.jakduk.core.service;

import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.UserPicture;
import com.jakduk.core.repository.user.UserPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pyohwan on 17. 2. 16.
 */

@Service
public class UserPictureService {

    @Autowired
    private UserPictureRepository userPictureRepository;

    public UserPicture findOneById(String id) {
        return userPictureRepository.findOneById(id)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER_IMAGE));
    }
}
