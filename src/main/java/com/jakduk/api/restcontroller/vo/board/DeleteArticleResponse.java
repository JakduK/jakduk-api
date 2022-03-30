package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.Constants;

/**
 * @author pyohwan
 *         16. 7. 21 오후 10:54
 */

public class DeleteArticleResponse {
	Constants.ARTICLE_DELETE_TYPE result;

	public DeleteArticleResponse() {
	}

	public DeleteArticleResponse(Constants.ARTICLE_DELETE_TYPE result) {
		this.result = result;
	}

	public Constants.ARTICLE_DELETE_TYPE getResult() {
		return result;
	}
}
