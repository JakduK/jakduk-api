package com.jakduk.api.repository;

import com.jakduk.api.model.db.Sequence;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SequenceRepository extends MongoRepository<Sequence, String>{
	Optional<Sequence> findOneByName(String name);
}
