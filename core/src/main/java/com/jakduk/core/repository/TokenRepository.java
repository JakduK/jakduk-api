package com.jakduk.core.repository;

import com.jakduk.core.model.db.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
	Optional<Token> findOneByCode(String code);
}
