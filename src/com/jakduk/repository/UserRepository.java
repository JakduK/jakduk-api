package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.User;
import com.jakduk.model.simple.OAuthUserOnLogin;
import com.jakduk.model.simple.UserProfile;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findById(String id);
	User findByUsername(String username);
	User findOneByUsername(String username);
	User findByEmail(String email);
	User findOneByEmail(String email);
	
	@Query(value="{'id' : ?0}")
	UserProfile userProfileFindById(String id);
	
	@Query(value="{$and : [ {'id' : {$ne : ?0}}, {'username' : ?1} ]}", fields="{'id' : 1, 'username' : 1}")
	UserProfile userFindByNEIdAndUsername(String id, String username);

	@Query(value="{'oauthUser.type' : ?0, 'oauthUser.oauthId' : ?1}")
	User userFindByOauthUser(String type, String oauthId);
	
	@Query(value="{'oauthUser.type' : ?0, 'oauthUser.oauthId' : ?1}")
	OAuthUserOnLogin findByOauthUser(String type, String oauthId);
	
	@Query(value="{'id' : ?0}", fields="{'username' : 1}")
	User writerFindById(String id);
}
