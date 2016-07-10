package com.jakduk.repository;

import com.jakduk.model.db.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
	Token findByCode(String code);
}
