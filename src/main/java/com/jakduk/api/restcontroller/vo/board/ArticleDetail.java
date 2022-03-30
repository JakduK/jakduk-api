package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 15 오후 10:24
 */

public class ArticleDetail {

	private String id; // 글ID
	private String board; // 게시판 ID
	private CommonWriter writer; // 글쓴이
	private String subject; // 글제목
	private Integer seq; // 글번호
	private String content; // 본문
	private BoardCategory category; // 말머리
	private Integer views; // 읽음 수
	private Integer numberOfLike; // 좋아요 수
	private Integer numberOfDislike; // 싫어요 수
	private ArticleStatus status; // 글상태
	private List<ArticleLog> logs; // 로그
	private List<ArticleGallery> galleries; // 그림 목록
	private Constants.FEELING_TYPE myFeeling; // 나의 감정 표현 종류

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BoardCategory getCategory() {
		return category;
	}

	public void setCategory(BoardCategory category) {
		this.category = category;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getNumberOfLike() {
		return numberOfLike;
	}

	public void setNumberOfLike(Integer numberOfLike) {
		this.numberOfLike = numberOfLike;
	}

	public Integer getNumberOfDislike() {
		return numberOfDislike;
	}

	public void setNumberOfDislike(Integer numberOfDislike) {
		this.numberOfDislike = numberOfDislike;
	}

	public ArticleStatus getStatus() {
		return status;
	}

	public void setStatus(ArticleStatus status) {
		this.status = status;
	}

	public List<ArticleLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ArticleLog> logs) {
		this.logs = logs;
	}

	public List<ArticleGallery> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<ArticleGallery> galleries) {
		this.galleries = galleries;
	}

	public Constants.FEELING_TYPE getMyFeeling() {
		return myFeeling;
	}

	public void setMyFeeling(Constants.FEELING_TYPE myFeeling) {
		this.myFeeling = myFeeling;
	}
}
