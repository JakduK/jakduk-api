package com.jakduk.api.restcontroller.vo.board;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.List;

/**
 * 글쓰기/글고치기 폼"
 *
 * @author pyohwan
 *         16. 7. 16 오후 7:55
 */

public class WriteArticle {

	@Size(min = 1, max = 60)
	@NotEmpty
	private String subject; // 글 제목

	@Size(min = 5)
	@NotEmpty
	private String content; // 글 내용

	private String categoryCode; // 말머리 코드
	private List<GalleryOnBoard> galleries; // 사진 목록

	public WriteArticle() {
	}

	public WriteArticle(@Size(min = 1, max = 60) @NotEmpty String subject, @Size(min = 5) @NotEmpty String content,
		String categoryCode, List<GalleryOnBoard> galleries) {
		this.subject = subject;
		this.content = content;
		this.categoryCode = categoryCode;
		this.galleries = galleries;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public List<GalleryOnBoard> getGalleries() {
		return galleries;
	}

	@Override
	public String toString() {
		return "WriteArticle{" +
			"subject='" + subject + '\'' +
			", content='" + content + '\'' +
			", categoryCode='" + categoryCode + '\'' +
			", galleries=" + galleries +
			'}';
	}
}
