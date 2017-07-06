package com.jakduk.api.vo.search;

import com.jakduk.core.model.elasticsearch.EsTermsBucket;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

@Builder
@Getter
public class PopularSearchWordResult {
	private Long took;
	private List<EsTermsBucket> popularSearchWords;
}
