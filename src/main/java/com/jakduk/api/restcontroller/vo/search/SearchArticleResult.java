package com.jakduk.api.restcontroller.vo.search;


import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

public class SearchArticleResult {
	private Long took; // 찾기에 걸린 시간(ms)
	private Long totalCount; // 매칭되는 아이템 수
	private List<ArticleSource> articles; // 매칭되는 게시물 목록

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

	public List<ArticleSource> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleSource> articles) {
		this.articles = articles;
	}
}
