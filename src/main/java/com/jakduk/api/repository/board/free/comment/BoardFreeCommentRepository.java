package com.jakduk.api.repository.board.free.comment;

import com.jakduk.api.model.db.BoardFreeComment;
import com.jakduk.api.model.embedded.BoardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 3.
 * @desc     :
 */
public interface BoardFreeCommentRepository extends MongoRepository<BoardFreeComment, String>, BoardFreeCommentRepositoryCustom {

	Optional<BoardFreeComment> findOneById(String id);

	Integer countByBoardItem(BoardItem boardItem);
	Page<BoardFreeComment> findByBoardItemBoard(String board, Pageable pageable);
	long count();

}