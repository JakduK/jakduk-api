package com.jakduk.api.repository.article;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.simple.ArticleOnList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     :
 */
public interface ArticleOnListRepository extends MongoRepository<ArticleOnList, String> {
	
	Page<ArticleOnList> findByBoard(Constants.BOARD_TYPE board, Pageable pageable);
	Page<ArticleOnList> findByBoardAndCategory(Constants.BOARD_TYPE board, String category, Pageable pageable);
}
