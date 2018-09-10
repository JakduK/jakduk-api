package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.SimpleWriter;

import java.time.LocalDateTime;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

public class EsSearchWord {

	private String id;
	private String word;
	private SimpleWriter writer;
	private LocalDateTime registerDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public SimpleWriter getWriter() {
		return writer;
	}

	public void setWriter(SimpleWriter writer) {
		this.writer = writer;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}
}
