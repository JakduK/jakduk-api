package com.jakduk.core.model.vo;

import com.jakduk.core.model.elasticsearch.ESBoardSource;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@Builder
@Getter
public class SearchBoardResult {

	private Long took;
	private Long totalCount;
	private List<ESBoardSource> posts;
}
