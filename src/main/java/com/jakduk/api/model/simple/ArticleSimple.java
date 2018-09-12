package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 26.
 * @desc     :
 */

@Document(collection = Constants.COLLECTION_ARTICLE)
public class ArticleSimple {
	
	@Id
	private String id;
	private Integer seq;
	private ArticleStatus status;
	private String board;
	private CommonWriter writer;
	private String subject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public ArticleStatus getStatus() {
		return status;
	}

	public void setStatus(ArticleStatus status) {
		this.status = status;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
