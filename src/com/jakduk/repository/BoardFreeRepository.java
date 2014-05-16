package com.jakduk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.BoardFree;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String>{

	Page<BoardFree> findAll(Pageable pageable);
	BoardFree findByWriter(String writer);
	long countByCategoryId(Integer categoryId);
	long count();
}
