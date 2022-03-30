package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleSimple;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 13 오후 11:19
 */

public class GetArticleComment {

	private String id; // 댓글ID
	private ArticleSimple article; // 연동 글
	private CommonWriter writer; // 글쓴이
	private String content; // 내용
	private Integer numberOfLike; // 좋아요 수
	private Integer numberOfDislike; // 싫어요 수
	private Constants.FEELING_TYPE myFeeling; // 나의 감정 표현 종류
	private List<BoardGallerySimple> galleries; // 그림 목록
	private List<ArticleCommentLog> logs; // 로그

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArticleSimple getArticle() {
		return article;
	}

	public void setArticle(ArticleSimple article) {
		this.article = article;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Constants.FEELING_TYPE getMyFeeling() {
		return myFeeling;
	}

	public void setMyFeeling(Constants.FEELING_TYPE myFeeling) {
		this.myFeeling = myFeeling;
	}

	public List<BoardGallerySimple> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<BoardGallerySimple> galleries) {
		this.galleries = galleries;
	}

	public List<ArticleCommentLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ArticleCommentLog> logs) {
		this.logs = logs;
	}
}
