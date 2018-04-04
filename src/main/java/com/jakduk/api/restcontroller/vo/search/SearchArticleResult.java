package com.jakduk.api.restcontroller.vo.search;


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
	private Long took; // 찾기에 걸린 시간(ms)
	private Long totalCount; // 매칭되는 아이템 수
	private List<ArticleSource> articles; // 매칭되는 게시물 목록
}
