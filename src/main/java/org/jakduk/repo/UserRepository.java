package org.jakduk.repo;

import org.jakduk.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByUserName(String userName);
}
