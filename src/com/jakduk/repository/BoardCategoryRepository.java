package com.jakduk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.BoardCategory;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public interface BoardCategoryRepository extends MongoRepository<BoardCategory, String> {
	
	BoardCategory findByUsingBoard(List<Integer> usingBoard);
	
}
