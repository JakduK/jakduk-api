package com.jakduk.api.repository.board.free;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.simple.BoardFreeOnList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     :
 */
public interface BoardFreeOnListRepository extends MongoRepository<BoardFreeOnList, String> {
	
	Page<BoardFreeOnList> findByBoard(Constants.BOARD_TYPE board, Pageable pageable);
	Page<BoardFreeOnList> findByBoardAndCategory(Constants.BOARD_TYPE board, String category, Pageable pageable);
}
