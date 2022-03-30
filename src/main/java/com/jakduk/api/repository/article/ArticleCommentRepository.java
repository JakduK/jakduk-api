package com.jakduk.api.repository.article;

import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.embedded.ArticleItem;
import org.bson.types.ObjectId;
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

	Integer countByArticle(ArticleItem articleItem);
	long count();

	// for JUnit
	Optional<ArticleComment> findTopByOrderByIdAsc();
	Optional<ArticleComment> findTopByIdLessThanEqualOrderByIdDesc(ObjectId id);

}