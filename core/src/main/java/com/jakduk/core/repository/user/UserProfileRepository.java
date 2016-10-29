package com.jakduk.core.repository.user;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.simple.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author pyohwan
 * 16. 4. 16 오후 10:54
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
