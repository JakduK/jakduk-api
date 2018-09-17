package com.jakduk.api.restcontroller.vo.search;

public class SearchUnifiedResponse {
	private SearchArticleResult articleResult; // 매칭된 글 객체
	private SearchCommentResult commentResult; // 매칭된 댓글 객체
	private SearchGalleryResult galleryResult; // 매칭된 사진 객체

	public SearchArticleResult getArticleResult() {
		return articleResult;
	}

	public void setArticleResult(SearchArticleResult articleResult) {
		this.articleResult = articleResult;
	}

	public SearchCommentResult getCommentResult() {
		return commentResult;
	}

	public void setCommentResult(SearchCommentResult commentResult) {
		this.commentResult = commentResult;
	}

	public SearchGalleryResult getGalleryResult() {
		return galleryResult;
	}

	public void setGalleryResult(SearchGalleryResult galleryResult) {
		this.galleryResult = galleryResult;
	}
}
