package com.jakduk.core.repository;

import com.jakduk.core.model.db.Sequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceRepository extends MongoRepository<Sequence, String>{
	Sequence findById(String id);
	Sequence findByName(Integer name);

}
