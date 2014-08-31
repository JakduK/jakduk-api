package com.jakduk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.BoardFree;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String> {

	Page<BoardFree> findAll(Pageable pageable);
	Page<BoardFree> findByCategoryId(Integer categoryId, Pageable pageable);
	BoardFree findOneBySeq(Integer seq);
	BoardFree findByWriter(String writer);
	long countByCategoryId(Integer categoryId);
	long count();
}
