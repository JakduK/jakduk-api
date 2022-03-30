package com.jakduk.api.restcontroller.vo.admin;

import com.jakduk.api.model.embedded.CommonWriter;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 24.
 * @desc     :
 */
public class BoardFreeWrite {

	@Id
	private String id;

	private CommonWriter writer;

	@NotNull
	@Size(min = 3, max = 60)
	private String subject;

	@NotNull
	@Size(min = 5)
	private String content;

	private int seq;

	@NotNull
	private String categoryName;

	private int views = 0;

	private String images;

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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "BoardFreeWrite [id=" + id + ", writer=" + writer + ", subject="
			+ subject + ", content=" + content + ", seq=" + seq
			+ ", category=" + categoryName + ", views=" + views
			+ ", images=" + images + "]";
	}

}
