package com.jakduk.api.repository.user;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.simple.UserOnPasswordUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
	
	Optional<User> findOneById(String id);
	Optional<User> findOneByEmail(String email);
	Optional<User> findOneByUsername(String username);
	Optional<User> findOneByProviderIdAndProviderUserId(Constants.ACCOUNT_TYPE providerId, String providerUserId);

	@Query(value="{'id' : ?0}")
	UserOnPasswordUpdate findUserOnPasswordUpdateById(String id);

}
