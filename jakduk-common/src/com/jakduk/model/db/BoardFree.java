package com.jakduk.model.db;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardHistory;
import com.jakduk.model.embedded.BoardImage;
import com.jakduk.model.embedded.BoardStatus;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;

/**
 * 자유게시판 모델
 * @author pyohwan
 *
 */
@Document
public class BoardFree {

	/**
	 * ID
	 */
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	/**
	 * 작성자
	 */
	
	private CommonWriter writer;
	
	/**
	 * 글 제목
	 */
	private String subject;
	
	/**
	 * 글 내용
	 */
	private String content;
	
	/**
	 * 글 번호
	 */
	private int seq;
	
	/**
	 * 분류 ID
	 */
	private String categoryName;
	
	/**
	 * 조회
	 */
	private int views;
	
	private List<CommonFeelingUser> usersLiking;
	
	private List<CommonFeelingUser> usersDisliking;
	
	private BoardStatus status;
	
	private List<BoardHistory> history;
	
	private List<BoardImage> galleries;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public List<CommonFeelingUser> getUsersLiking() {
		return usersLiking;
	}

	public void setUsersLiking(List<CommonFeelingUser> usersLiking) {
		this.usersLiking = usersLiking;
	}

	public List<CommonFeelingUser> getUsersDisliking() {
		return usersDisliking;
	}

	public void setUsersDisliking(List<CommonFeelingUser> usersDisliking) {
		this.usersDisliking = usersDisliking;
	}

	public BoardStatus getStatus() {
		return status;
	}

	public void setStatus(BoardStatus status) {
		this.status = status;
	}

	public List<BoardHistory> getHistory() {
		return history;
	}

	public void setHistory(List<BoardHistory> history) {
		this.history = history;
	}

	public List<BoardImage> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<BoardImage> galleries) {
		this.galleries = galleries;
	}

	@Override
	public String toString() {
		return "BoardFree [id=" + id + ", writer=" + writer + ", subject="
				+ subject + ", content=" + content + ", seq=" + seq
				+ ", categoryName=" + categoryName + ", views=" + views
				+ ", usersLiking=" + usersLiking + ", usersDisliking="
				+ usersDisliking + ", status=" + status + ", history="
				+ history + ", galleries=" + galleries + "]";
	}
	

}
