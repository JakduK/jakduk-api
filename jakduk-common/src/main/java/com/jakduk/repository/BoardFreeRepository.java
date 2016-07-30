package com.jakduk.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.BoardFree;
import com.jakduk.model.simple.BoardFreeOfMinimum;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.model.web.BoardFreeWrite;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String> {

	Optional<BoardFree> findOneById(String id);
	Optional<BoardFree> findOneBySeq(Integer seq);

	Page<BoardFreeOnList> findByCategoryName(String categoryName, Pageable pageable);
	BoardFree findByWriter(String writer);
	long countByCategoryName(String categoryName);
	long count();
	
	@Query(value="{'status.notice' : true}")
	Page<BoardFreeOnList> findByNotice(Pageable pageable);
	
	@Query(value="{'seq' : ?0}")
	BoardFreeWrite boardFreeWriteFindOneBySeq(Integer seq);
	
	@Query(value="{'seq' : ?0}")
	BoardFreeOfMinimum findBoardFreeOfMinimumBySeq(Integer seq);
}
