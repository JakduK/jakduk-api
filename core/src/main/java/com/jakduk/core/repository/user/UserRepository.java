package com.jakduk.core.repository.user;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.simple.OAuthProfile;
import com.jakduk.core.model.simple.SocialUserOnAuthentication;
import com.jakduk.core.model.simple.UserOnPasswordUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
	
	Optional<User> findOneById(String id);
	Optional<User> findOneByEmail(String email);
	Optional<User> findOneByUsername(String username);
	Optional<User> findOneByProviderIdAndProviderUserId(CoreConst.ACCOUNT_TYPE providerId, String providerUserId);

	@Query(value="{'id' : ?0}", fields="{'username' : 1}")
	User writerFindById(String id);

	@Query(value="{'id' : ?0}")
	UserOnPasswordUpdate findUserOnPasswordUpdateById(String id);

	@Query(value="{'email' : ?0}")
	SocialUserOnAuthentication findSocialUserByEmail(String email);

	@Query(value="{'socialInfo.providerId' : ?0, 'socialInfo.oauthId' : ?1}")
	SocialUserOnAuthentication findByOauthUser(CoreConst.ACCOUNT_TYPE providerId, String oauthId);
	
	@Query(value="{'socialInfo.oauthId' : {$ne : ?0}, 'username' : ?1}", fields="{'id' : 1, 'username' : 1, 'socialInfo' : 1}")
	OAuthProfile userFindByNEOauthIdAndUsername(String oauthId, String username);
	
	@Query(value="{'socialInfo.providerId' : ?0, 'socialInfo.oauthId' : ?1}")
	OAuthProfile userfindByOauthUser(CoreConst.ACCOUNT_TYPE providerId, String oauthId);

}
