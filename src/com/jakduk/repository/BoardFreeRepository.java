package com.jakduk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.BoardFree;
import com.jakduk.model.simple.BoardFreeOnComment;
import com.jakduk.model.simple.BoardFreeOnList;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String> {

	Page<BoardFreeOnList> findByCategoryName(String categoryName, Pageable pageable);
	
	@Query(value="{'status.notice' : 'notice'}")
	Page<BoardFreeOnList> findByNotice(Pageable pageable);
	
	BoardFree findOneBySeq(Integer seq);
	BoardFree findByWriter(String writer);
	long countByCategoryName(String categoryName);
	long count();
	
	@Query(value="{'seq' : ?0}")
	BoardFreeOnComment boardFreeOnCommentFindBySeq(Integer seq);
}
