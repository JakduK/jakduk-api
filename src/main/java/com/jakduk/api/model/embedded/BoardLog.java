package com.jakduk.api.model.embedded;

import org.springframework.data.annotation.Id;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

public class BoardLog {

	@Id
	private String id;
	private String type;
	private SimpleWriter writer;

	public BoardLog() {
	}

	public BoardLog(String id, String type, SimpleWriter writer) {
		this.id = id;
		this.type = type;
		this.writer = writer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SimpleWriter getWriter() {
		return writer;
	}

	public void setWriter(SimpleWriter writer) {
		this.writer = writer;
	}
}
