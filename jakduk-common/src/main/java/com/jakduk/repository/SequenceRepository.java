package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.Sequence;

public interface SequenceRepository extends MongoRepository<Sequence, String>{
	Sequence findById(String id);
	Sequence findByName(Integer name);

}
