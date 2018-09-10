package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;
import java.util.Map;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

public class EsArticleSource {

	private String id;
	private Integer seq;
	private String board;
	private String category;
	private CommonWriter writer;
	private String subject;
	private String content;
	private List<String> galleries;
	private Float score;
	private Map<String, List<String>> highlight;

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

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<String> galleries) {
		this.galleries = galleries;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Map<String, List<String>> getHighlight() {
		return highlight;
	}

	public void setHighlight(Map<String, List<String>> highlight) {
		this.highlight = highlight;
	}
}
