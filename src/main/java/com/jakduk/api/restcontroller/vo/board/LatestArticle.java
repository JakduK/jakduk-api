package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;

/**
 * 글쓴이의 최근글
 *
 * Created by pyohwanjang on 2017. 3. 10..
 */

public class LatestArticle {

	private String id; // 글ID
	private Integer seq; // 글번호
	private CommonWriter writer; // 글쓴이
	private String subject; // 글제목
	private List<BoardGallerySimple> galleries; // 그림 목록

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

	public List<BoardGallerySimple> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<BoardGallerySimple> galleries) {
		this.galleries = galleries;
	}
}
