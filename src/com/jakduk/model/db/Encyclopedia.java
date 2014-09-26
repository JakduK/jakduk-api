package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */

@Document
public class Encyclopedia {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	/**
	 *  종류. 1이면 최고의 선수, 2이면 추천 책
	 */
	private Integer kind;
	
	private String language;
	
	@NotNull
	private String subject;
	
	@NotNull
	private String content;
	
	private int seq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	@Override
	public String toString() {
		return "Encyclopedia [id=" + id + ", kind=" + kind + ", language="
				+ language + ", subject=" + subject + ", content=" + content
				+ ", seq=" + seq + "]";
	}

}
