package com.jakduk.api.restcontroller.vo.admin;

/**
 * @author pyohwan
 * 16. 3. 13 오후 11:14
 */

public class JakduCommentWriteRequest {
	private String contents;            // 댓글 내용
	private String id;                  // 작두일정 ID

	public String getContents() {
		return contents;
	}

	public String getId() {
		return id;
	}
}
