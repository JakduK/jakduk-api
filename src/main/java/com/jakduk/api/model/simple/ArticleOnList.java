package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     : 각종 목록에서 쓰임
 */

@Document(collection = Constants.COLLECTION_ARTICLE)
public class ArticleOnList {
	
	@Id
	private String id;
	private Integer seq;
	private String board;
	private String category;
	private ArticleStatus status;
	private CommonWriter writer;
	private String subject;
	private Integer views;
	private String shortContent;
	private Boolean linkedGallery;

	public String getId() {
		return id;
	}

	public Integer getSeq() {
		return seq;
	}

	public String getBoard() {
		return board;
	}

	public String getCategory() {
		return category;
	}

	public ArticleStatus getStatus() {
		return status;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public String getSubject() {
		return subject;
	}

	public Integer getViews() {
		return views;
	}

	public String getShortContent() {
		return shortContent;
	}

	public Boolean getLinkedGallery() {
		return linkedGallery;
	}
}
