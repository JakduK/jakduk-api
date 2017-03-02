package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeOnList;
import com.jakduk.core.model.web.board.BoardFreeWrite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String>, BoardFreeRepositoryCustom {

	Optional<BoardFree> findOneById(String id);
	Optional<BoardFree> findOneBySeq(Integer seq);

	@Query(value="{'seq' : ?0}")
    BoardFreeOfMinimum findBoardFreeOfMinimumBySeq(Integer seq);
}
