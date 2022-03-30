package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 24.
 * @desc     :
 */

@Document(collection = Constants.COLLECTION_ARTICLE)
public class ArticleOnRSS {

	private String id;
	private String board;
	private Integer seq;
	private CommonWriter writer;
	private String subject;
	private String content;

	public String getId() {
		return id;
	}

	public String getBoard() {
		return board;
	}

	public Integer getSeq() {
		return seq;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}
}
