package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;

import java.time.LocalDateTime;

public class ArticleLog {

	private String id;
	private Constants.ARTICLE_LOG_TYPE type;
	private CommonWriter writer; // 글쓴이
	private LocalDateTime timestamp; // 2017-07-18T00:25:45 Timestamp (ISO 8601)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Constants.ARTICLE_LOG_TYPE getType() {
		return type;
	}

	public void setType(Constants.ARTICLE_LOG_TYPE type) {
		this.type = type;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
