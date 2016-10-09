package com.jakduk.core.repository.board.free;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.embedded.BoardItem;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 3.
 * @desc     :
 */
public interface BoardFreeCommentRepository extends MongoRepository<BoardFreeComment, String>{

	Integer countByBoardItem(BoardItem boardItem);
	BoardFreeComment findById(String id);
	Page<BoardFreeComment> findAll(Pageable pageable);
	long count();
}