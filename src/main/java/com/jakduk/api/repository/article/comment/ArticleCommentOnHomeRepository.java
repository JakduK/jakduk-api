package com.jakduk.api.repository.article.comment;

import com.jakduk.api.model.simple.ArticleCommentOnHome;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 27.
 * @desc     :
 */
public interface ArticleCommentOnHomeRepository extends MongoRepository<ArticleCommentOnHome, String>{

}
