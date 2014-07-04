package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByUsername(String username);
	User findByEmail(String email);
	User findOneByEmail(String email);
	User findById(String id);
	Integer countByEmail(String email);
	Integer countByUsername(String username);
	
	@Query(value="{ 'id' : ?0 }", fields="{ 'username' : 1}")
	User writerFindById(String id);
}
