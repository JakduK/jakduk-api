package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByUserName(String userName);
}
