package com.jakduk.api.restcontroller.vo.search;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@Builder
@Getter
public class SearchArticleResult {

	@ApiModelProperty(example = "330", value = "찾기에 걸린 시간(ms)")
	private Long took;

	@ApiModelProperty(example = "9", value = "매칭되는 아이템 수")
	private Long totalCount;

	@ApiModelProperty(value = "매칭되는 게시물 목록")
	private List<ArticleSource> articles;

}
