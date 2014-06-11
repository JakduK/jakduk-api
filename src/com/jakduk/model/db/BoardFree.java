package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
	@DBRef
	private BoardWriter writer;
	
	/**
	 * 글 제목
	 */
	@NotNull
	@Size(min = 1, message="Input subject")
	private String subject;
	
	/**
	 * 글 내용
	 */
	@NotNull
	@Size(min = 1, message="Input contents")
	private String content;
	
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
	private int views;
	
	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getSeq() {
		return seq;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "BoardFree [id=" + id + ", writer=" + writer + ", subject="
				+ subject + ", content=" + content + ", seq=" + seq
				+ ", categoryId=" + categoryId + ", views=" + views + "]";
	}

}
