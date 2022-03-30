package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.elasticsearch.EsGallerySource;

import java.util.List;

/**
 * Created by pyohwan on 16. 12. 5.
 */

public class SearchGalleryResult {

	private Long took;
	private Long totalCount;
	private List<EsGallerySource> galleries;

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

	public List<EsGallerySource> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<EsGallerySource> galleries) {
		this.galleries = galleries;
	}
}
