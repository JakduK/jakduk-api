package com.jakduk.core.repository;

import com.jakduk.core.model.db.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
	Token findByCode(String code);
}
