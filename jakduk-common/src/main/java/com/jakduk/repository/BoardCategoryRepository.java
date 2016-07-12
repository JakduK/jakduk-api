package com.jakduk.repository;

import com.jakduk.model.db.BoardCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public interface BoardCategoryRepository extends MongoRepository<BoardCategory, String> {
	BoardCategory findOneByCode(String code);

}
