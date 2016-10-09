package com.jakduk.core.repository.board.free;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.core.model.simple.BoardFreeOnHome;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */
public interface BoardFreeOnHomeRepository extends MongoRepository<BoardFreeOnHome, String> {
	Page<BoardFreeOnHome> findAll(Pageable pageable);
}
