package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.elasticsearch.EsCommentSource;

import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

public class SearchCommentResult {

	private Long took;
	private Long totalCount;
	private List<EsCommentSource> comments;

	public Long getTook() {
		return took;
	}

	public void setTook(Long took) {
		this.took = took;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<EsCommentSource> getComments() {
		return comments;
	}

	public void setComments(List<EsCommentSource> comments) {
		this.comments = comments;
	}
}
