package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 27.
 * @desc     :
 */

@Document(collection = Constants.COLLECTION_ARTICLE_COMMENT)
public class ArticleCommentSimple {
	
	@Id
	private String id;
	private ArticleItem article;
	private CommonWriter writer;
	private String content;

	public String getId() {
		return id;
	}

	public ArticleItem getArticle() {
		return article;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
