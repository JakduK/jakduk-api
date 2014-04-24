package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.BoardFree;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String>{

	BoardFree findByWriter(String writer);
}
