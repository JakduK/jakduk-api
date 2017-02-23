package com.jakduk.core.service;

import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.UserImage;
import com.jakduk.core.repository.user.UserImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pyohwan on 17. 2. 16.
 */

@Service
public class UserImageService {

    @Autowired
    private UserImageRepository userImageRepository;

    public UserImage findOneById(String id) {
        return userImageRepository.findOneById(id)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER_IMAGE));
    }
}
