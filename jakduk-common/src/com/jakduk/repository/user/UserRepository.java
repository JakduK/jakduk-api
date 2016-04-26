package com.jakduk.repository.user;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findById(String id);
	User findByUsername(String username);
	User findOneByUsername(String username);
	User findByEmail(String email);
	User findOneByEmail(String email);
	
	@Query(value="{'id' : ?0}", fields="{'username' : 1}")
	User writerFindById(String id);

	@Query(value="{'id' : ?0}")
	UserOnPasswordUpdate userOnPasswordUpdateFindById(String id);

	// 해당 ID를 제외하고 email과 일치하는 회원 찾기.
	@Query(value="{'id' : {$ne : ?0}, 'email' : ?1}", fields="{'id' : 1, 'email' : 1}")
	UserProfile findByNEIdAndEmail(String id, String email);

	// 해당 ID를 제외하고 username과 일치하는 회원 찾기.
	@Query(value="{'id' : {$ne : ?0}, 'username' : ?1}", fields="{'id' : 1, 'username' : 1}")
	UserProfile findByNEIdAndUsername(String id, String username);

	// SNS 계정으로 가입한 회원 찾기.
	UserProfile findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE providerId, String providerUserId);

	@Query(value="{'socialInfo.providerId' : ?0, 'socialInfo.oauthId' : ?1}")
	User userFindByOauthUser(CommonConst.ACCOUNT_TYPE providerId, String oauthId);

	@Query(value="{'email' : ?0}")
	SocialUserOnAuthentication findSocialUserByEmail(String email);

	@Query(value="{'socialInfo.providerId' : ?0, 'socialInfo.oauthId' : ?1}")
	SocialUserOnAuthentication findByOauthUser(CommonConst.ACCOUNT_TYPE providerId, String oauthId);
	
	@Query(value="{'socialInfo.oauthId' : {$ne : ?0}, 'username' : ?1}", fields="{'id' : 1, 'username' : 1, 'socialInfo' : 1}")
	OAuthProfile userFindByNEOauthIdAndUsername(String oauthId, String username);
	
	@Query(value="{'socialInfo.providerId' : ?0, 'socialInfo.oauthId' : ?1}")
	OAuthProfile userfindByOauthUser(CommonConst.ACCOUNT_TYPE providerId, String oauthId);
	
	@Query(value="{'email' : ?0}")
	UserOnAuthentication userFindByEmail(String email);

	@Query(value="{'providerUserId' : ?0}")
	UserOnAuthentication userFindByProviderUserId(String providerUserId);
}
