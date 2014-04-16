package org.jakduk.repository;

import org.jakduk.model.BoardFree;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String>{

	BoardFree findByWriter(String writer);
}
