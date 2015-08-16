package com.jakduk.model.elasticsearch;

import com.jakduk.model.embedded.CommonWriter;

import io.searchbox.annotations.JestId;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 3.
* @desc     :
*/
public class BoardFreeOnES {
	
	@JestId
    private String id;
	
	private CommonWriter writer;
	
	private String subject;
	
	private String content;
	
	private String contentPreview;
	
	private int seq;
	
	private String categoryName;

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

	public String getContentPreview() {
		return contentPreview;
	}

	public void setContentPreview(String contentPreview) {
		this.contentPreview = contentPreview;
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

	@Override
	public String toString() {
		return "BoardFreeOnES [id=" + id + ", writer=" + writer + ", subject=" + subject + ", content=" + content
				+ ", contentPreview=" + contentPreview + ", seq=" + seq + ", categoryName=" + categoryName + "]";
	}
	
}
