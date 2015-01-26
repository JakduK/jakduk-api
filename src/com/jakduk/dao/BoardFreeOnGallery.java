package com.jakduk.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 26.
 * @desc     :
 */

@Document(collection = "boardFree")
public class BoardFreeOnGallery {
	
	@Id
	private String id;
	
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
		return "BoardFreeOnGallery [id=" + id + ", subject=" + subject
				+ ", seq=" + seq + "]";
	}
	

}
