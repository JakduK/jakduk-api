package com.jakduk.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.CommonWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 26.
 * @desc     :
 */

@Document(collection = "boardFree")
public class BoardFreeOnGallery {
	
	/**
	 * ID
	 */
	@Id
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
	 * 글 번호
	 */
	private int seq;

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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "BoardFreeOnGallery [id=" + id + ", writer=" + writer
				+ ", subject=" + subject + ", seq=" + seq + "]";
	}

}
