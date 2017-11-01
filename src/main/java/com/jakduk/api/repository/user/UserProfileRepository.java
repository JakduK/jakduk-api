package com.jakduk.api.repository.user;

import com.jakduk.api.model.simple.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * @author pyohwan
 * 16. 4. 16 오후 10:54
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {

    Optional<UserProfile> findOneById(String id);
    Optional<UserProfile> findOneByEmail(String email);
    Optional<UserProfile> findOneByUsername(String username);

    // 해당 ID를 제외하고 email과 일치하는 회원 찾기.
    @Query(value="{'id' : {$ne : ?0}, 'email' : ?1}", fields="{'id' : 1, 'email' : 1}")
    Optional<UserProfile> findByNEIdAndEmail(String id, String email);

    // 해당 ID를 제외하고 username과 일치하는 회원 찾기.
    @Query(value="{'id' : {$ne : ?0}, 'username' : ?1}", fields="{'id' : 1, 'username' : 1}")
    Optional<UserProfile> findByNEIdAndUsername(String id, String username);
}
