package com.jakduk.model.simple;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardUser;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     :
 */

@Document(collection = "boardFree")
public class BoardFreeOnList {
	
	/**
	 * ID
	 */
	@Id
	private String id;

	/**
	 * 작성자
	 */
	@DBRef
	private BoardWriter writer;
	
	/**
	 * 글 제목
	 */
	@NotNull
	@Size(min = 1, message="Input subject")
	private String subject;
	
	/**
	 * 글 번호
	 */
	private int seq;
	
	/**
	 * 분류 ID
	 */
	@NotNull(message = "Input category")
	private Integer categoryId;
	
	/**
	 * 조회
	 */
	private int views = 0;
	
	private List<BoardUser> goodUsers;
	
	private List<BoardUser> badUsers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BoardWriter getWriter() {
		return writer;
	}

	public void setWriter(BoardWriter writer) {
		this.writer = writer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public List<BoardUser> getGoodUsers() {
		return goodUsers;
	}

	public void setGoodUsers(List<BoardUser> goodUsers) {
		this.goodUsers = goodUsers;
	}

	public List<BoardUser> getBadUsers() {
		return badUsers;
	}

	public void setBadUsers(List<BoardUser> badUsers) {
		this.badUsers = badUsers;
	}

	@Override
	public String toString() {
		return "BoardFreeOnList [id=" + id + ", writer=" + writer
				+ ", subject=" + subject + ", seq=" + seq + ", categoryId="
				+ categoryId + ", views=" + views + ", goodUsers=" + goodUsers
				+ ", badUsers=" + badUsers + "]";
	}

}
