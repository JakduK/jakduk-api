package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.BoardSequence;

public interface BoardSequenceRepository extends MongoRepository<BoardSequence, String>{
	BoardSequence findById(String id);
	BoardSequence findByName(String name);

}
