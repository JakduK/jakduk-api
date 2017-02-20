package com.jakduk.core.repository.user;

import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserImage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 17. 2. 16.
 */
public interface UserImageRepository extends MongoRepository<UserImage, String> {
}
