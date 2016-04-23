package com.jakduk.repository.user;

import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 16. 4. 16.
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findOneByEmail(String email);
    UserProfile findOneByUsername(String username);
}
