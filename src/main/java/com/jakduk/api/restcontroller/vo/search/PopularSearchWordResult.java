package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.elasticsearch.EsTermsBucket;

import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

public class PopularSearchWordResult {
	private Long took;
	private List<EsTermsBucket> popularSearchWords;

	public Long getTook() {
		return took;
	}

	public void setTook(Long took) {
		this.took = took;
	}

	public List<EsTermsBucket> getPopularSearchWords() {
		return popularSearchWords;
	}

	public void setPopularSearchWords(List<EsTermsBucket> popularSearchWords) {
		this.popularSearchWords = popularSearchWords;
	}
}
