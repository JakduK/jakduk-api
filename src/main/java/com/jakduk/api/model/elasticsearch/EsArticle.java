package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 8. 3.
 * @desc     :
 */

public class EsArticle {

	private String id;
	private Integer seq;
	private String board;
	private String category;
	private CommonWriter writer;
	private String subject;
	private String content;
	private List<String> galleries;

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
}
