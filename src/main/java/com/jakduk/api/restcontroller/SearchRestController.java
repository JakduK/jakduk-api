package com.jakduk.api.restcontroller;

import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.service.SearchService;
import com.jakduk.api.restcontroller.vo.search.PopularSearchWordResult;
import com.jakduk.api.restcontroller.vo.search.SearchUnifiedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Slf4j
@Validated
@Api(tags = "Search", description = "찾기 API")
@RequestMapping("/api/search")
@RestController
public class SearchRestController {
	
	@Autowired private RabbitMQPublisher rabbitMQPublisher;
	@Autowired private SearchService searchService;

	@ApiOperation("통합 찾기")
	@GetMapping("")
	public SearchUnifiedResponse searchUnified(
			@ApiParam(value = "검색어", required = true) @NotEmpty @RequestParam String q,
			@ApiParam(value = "ARTICLE;COMMENT;GALLERY", required = true) @NotEmpty @RequestParam(defaultValue = "ARTICLE;COMMENT;GALLERY") String w,
			@ApiParam(value = "페이지 시작 위치") @RequestParam(required = false, defaultValue = "0") Integer from,
			@ApiParam(value = "페이지 크기") @RequestParam(required = false, defaultValue = "10") Integer size,
			@ApiParam(value = "하이라이트의 태그") @RequestParam(required = false) String tag,
			@ApiParam(value = "하이라이트의 태그 클래스") @RequestParam(required = false) String styleClass) {

		log.debug("unified search request q={}, w={}, from={}, size={}, tag={}, styleClass={}", q, w, from, size, tag, styleClass);

		if (size <= 0) size = 10;

		String preTags = StringUtils.EMPTY;
		String postTags = StringUtils.EMPTY;

		if (StringUtils.isNotBlank(tag)) {
			if (StringUtils.isNotBlank(styleClass)) {
				preTags = String.format("<%s class=\"%s\">", tag, styleClass);
			} else {
				preTags = String.format("<%s>", tag);
			}

			postTags = String.format("</%s>", tag);
		}

		SearchUnifiedResponse searchUnifiedResponse = searchService.searchUnified(q, w, from, size, preTags, postTags);

		rabbitMQPublisher.indexDocumentSearchWord(StringUtils.lowerCase(q), AuthUtils.getCommonWriter());

		return searchUnifiedResponse;
	}

	@ApiOperation("인기 검색어")
	@GetMapping("/popular-words")
	public PopularSearchWordResult searchPopularWords(
			@ApiParam(value = "크기") @RequestParam(required = false, defaultValue = "5") Integer size) {

		// 3 주전
		LocalDate threeWeeksAgo = LocalDate.now().minusWeeks(3);

		return searchService.aggregateSearchWord(threeWeeksAgo, size);
	}

}
