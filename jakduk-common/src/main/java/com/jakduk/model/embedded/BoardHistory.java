package com.jakduk.model.embedded;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */
public class BoardHistory {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String type;
	
	private CommonWriter writer;

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

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	@Override
	public String toString() {
		return "BoardHistory [id=" + id + ", type=" + type + ", writer="
				+ writer + "]";
	}
	
}
