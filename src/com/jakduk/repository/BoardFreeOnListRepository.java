package com.jakduk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.simple.BoardFreeOnList;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     :
 */
public interface BoardFreeOnListRepository extends MongoRepository<BoardFreeOnList, String> {
	
	Page<BoardFreeOnList> findAll(Pageable pageable);
	Page<BoardFreeOnList> findByCategoryId(Integer categoryId, Pageable pageable);
	long countByCategoryId(Integer categoryId);
	long count();
}
