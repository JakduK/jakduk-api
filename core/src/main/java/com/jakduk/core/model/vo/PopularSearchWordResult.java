package com.jakduk.core.model.vo;

import com.jakduk.core.model.elasticsearch.ESTermsBucket;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 12. 26.
 */

@Builder
@Getter
public class PopularSearchWordResult {
	private Long took;
	private List<ESTermsBucket> popularSearchWords;
}
