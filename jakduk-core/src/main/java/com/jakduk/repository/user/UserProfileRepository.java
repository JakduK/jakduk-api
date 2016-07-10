package com.jakduk.repository.user;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by pyohwan on 16. 4. 16.
 */
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findOneByEmail(String email);
    UserProfile findOneByUsername(String username);

    // SNS 계정으로 가입한 회원 찾기.
    UserProfile findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE providerId, String providerUserId);

    // 해당 ID를 제외하고 email과 일치하는 회원 찾기.
    @Query(value="{'id' : {$ne : ?0}, 'email' : ?1}", fields="{'id' : 1, 'email' : 1}")
    UserProfile findByNEIdAndEmail(String id, String email);

    // 해당 ID를 제외하고 username과 일치하는 회원 찾기.
    @Query(value="{'id' : {$ne : ?0}, 'username' : ?1}", fields="{'id' : 1, 'username' : 1}")
    UserProfile findByNEIdAndUsername(String id, String username);
}
