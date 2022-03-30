package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 3..
 */

public class HomeArticle {

	private String id; // 글ID
	private CommonWriter writer; // 글쓴이
	private String subject; // 글제목
	private Integer seq; // 글번호
	private String board; // 게시판
	private String category; // 말머리
	private Integer views; // 읽음 수
	private ArticleStatus status; // 글상태
	private List<BoardGallerySimple> galleries; // 그림 목록
	private String shortContent; // 본문 100자

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public ArticleStatus getStatus() {
		return status;
	}

	public void setStatus(ArticleStatus status) {
		this.status = status;
	}

	public List<BoardGallerySimple> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<BoardGallerySimple> galleries) {
		this.galleries = galleries;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}
}
