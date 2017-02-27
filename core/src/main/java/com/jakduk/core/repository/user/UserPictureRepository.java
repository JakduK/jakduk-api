package com.jakduk.core.repository.user;

import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserPicture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by pyohwan on 17. 2. 16.
 */
public interface UserPictureRepository extends MongoRepository<UserPicture, String> {

    Optional<UserPicture> findOneById(String id);
}
