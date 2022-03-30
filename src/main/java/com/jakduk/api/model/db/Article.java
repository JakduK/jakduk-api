package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 자유게시판 모델
 * @author pyohwan
 *
 */

@Document
public class Article implements UsersFeeling {

	@Id
	private String id;
	private Integer seq; // 글 번호
	private String board; // 게시판
	private String category; // 말머리 코드
	private ArticleStatus status;
	private CommonWriter writer; // 작성자
	private String subject; // 글 제목
	private String content; // 글 내용
	private Integer views; // 읽음 수
	private List<CommonFeelingUser> usersLiking;
	private List<CommonFeelingUser> usersDisliking;
	private List<BoardLog> logs; // 오래된 글은 logs가 없는 경우도 있다.
	private List<String> batch;
	private String shortContent;
	private LocalDateTime lastUpdated;
	private Boolean linkedGallery;

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

	public ArticleStatus getStatus() {
		return status;
	}

	public void setStatus(ArticleStatus status) {
		this.status = status;
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

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	@Override
	public List<CommonFeelingUser> getUsersLiking() {
		return usersLiking;
	}

	@Override
	public void setUsersLiking(List<CommonFeelingUser> usersLiking) {
		this.usersLiking = usersLiking;
	}

	@Override
	public List<CommonFeelingUser> getUsersDisliking() {
		return usersDisliking;
	}

	@Override
	public void setUsersDisliking(List<CommonFeelingUser> usersDisliking) {
		this.usersDisliking = usersDisliking;
	}

	public List<BoardLog> getLogs() {
		return logs;
	}

	public void setLogs(List<BoardLog> logs) {
		this.logs = logs;
	}

	public List<String> getBatch() {
		return batch;
	}

	public void setBatch(List<String> batch) {
		this.batch = batch;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Boolean getLinkedGallery() {
		return linkedGallery;
	}

	public void setLinkedGallery(Boolean linkedGallery) {
		this.linkedGallery = linkedGallery;
	}
}
