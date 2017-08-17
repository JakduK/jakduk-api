package com.jakduk.api.repository.board.free;

import com.jakduk.api.model.db.BoardFree;
import com.jakduk.api.model.simple.BoardFreeSimple;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardFreeRepository extends MongoRepository<BoardFree, String>, BoardFreeRepositoryCustom {

	Optional<BoardFree> findOneById(String id);
	Optional<BoardFree> findOneBySeq(Integer seq);
	Optional<BoardFree> findTopByOrderByIdAsc();
	Optional<BoardFree> findTopByIdLessThanEqualOrderByIdDesc(ObjectId id);
	List<BoardFree> findByIdInAndLinkedGalleryIsTrue(List<String> ids);
	List<BoardFree> findByIdInAndBoard(List<String> ids, String board);

	@Query(value="{'seq' : ?0}")
	BoardFreeSimple findBoardFreeOfMinimumBySeq(Integer seq);

}
