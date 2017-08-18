package com.jakduk.api.repository.article.comment;

import com.jakduk.api.model.db.ArticleComment;
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
public interface ArticleCommentRepository extends MongoRepository<ArticleComment, String>, ArticleCommentRepositoryCustom {

	Optional<ArticleComment> findOneById(String id);

	Integer countByBoardItem(BoardItem boardItem);
	Page<ArticleComment> findByBoardItemBoard(String board, Pageable pageable);
	long count();

}