package com.jakduk.api.restcontroller.vo.board;

import java.util.List;

/**
 * 특정 글의 댓글 목록
 *
 * @author pyohwan
 * 16. 3. 23 오후 11:18
 */

public class GetArticleDetailCommentsResponse {
	private List<GetArticleComment> comments; // 댓글 목록
	private Integer count; // 댓글 수

	public GetArticleDetailCommentsResponse() {
	}

	public GetArticleDetailCommentsResponse(List<GetArticleComment> comments, Integer count) {
		this.comments = comments;
		this.count = count;
	}

	public List<GetArticleComment> getComments() {
		return comments;
	}

	public Integer getCount() {
		return count;
	}
}
