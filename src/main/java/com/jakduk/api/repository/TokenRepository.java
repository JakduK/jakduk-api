package com.jakduk.api.repository;

import com.jakduk.api.model.db.Token;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {

	Optional<Token> findOneByEmail(String email);

	Optional<Token> findOneByCode(String code);

}
