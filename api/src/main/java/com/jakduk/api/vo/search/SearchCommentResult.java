package com.jakduk.api.vo.search;

import com.jakduk.core.model.elasticsearch.EsCommentSource;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@Builder
@Getter
public class SearchCommentResult {

	private Long took;
	private Long totalCount;
	private List<EsCommentSource> comments;
}
