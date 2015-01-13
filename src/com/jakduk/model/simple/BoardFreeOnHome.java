package com.jakduk.model.simple;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardStatus;
import com.jakduk.model.embedded.BoardWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@Document(collection = "boardFree")
public class BoardFreeOnHome {
	
	@Id
	private String id;
	
	/**
	 * 작성자
	 */
	private BoardWriter writer;
	
	/**
	 * 글 제목
	 */
	private String subject;
	
	/**
	 * 글 번호
	 */
	private int seq;
	
	private BoardStatus status;

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

	public BoardStatus getStatus() {
		return status;
	}

	public void setStatus(BoardStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BoardFreeOnHome [id=" + id + ", writer=" + writer
				+ ", subject=" + subject + ", seq=" + seq + ", status="
				+ status + "]";
	}
	
}
