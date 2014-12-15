package com.jakduk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.simple.BoardFreeOnComment;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 3.
 * @desc     :
 */
public interface BoardFreeCommentRepository extends MongoRepository<BoardFreeComment, String>{

	Page<BoardFreeComment> findByBoardFree(BoardFreeOnComment boardFree, Pageable pageable);
	Integer countByBoardFree(BoardFreeOnComment boardFree);
}
