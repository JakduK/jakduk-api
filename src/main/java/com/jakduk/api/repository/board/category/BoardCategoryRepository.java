package com.jakduk.api.repository.board.category;


import com.jakduk.api.model.db.BoardCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public interface BoardCategoryRepository extends MongoRepository<BoardCategory, String>, BoardCategoryCustom {

	Optional<BoardCategory> findOneByCode(String code);

}
