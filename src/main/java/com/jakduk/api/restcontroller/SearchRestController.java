package com.jakduk.api.restcontroller;

import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.restcontroller.vo.search.PopularSearchWordResult;
import com.jakduk.api.restcontroller.vo.search.SearchUnifiedResponse;
import com.jakduk.api.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * 찾기 API
 *
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Validated
@RequestMapping("/api/search")
@RestController
public class SearchRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private RabbitMQPublisher rabbitMQPublisher;
	@Autowired private SearchService searchService;

	// 통합 찾기
	@GetMapping("")
	public SearchUnifiedResponse searchUnified(
			@NotEmpty @RequestParam String q, // 검색어
			@NotEmpty @RequestParam(defaultValue = "ARTICLE;COMMENT;GALLERY") String w, // ARTICLE;COMMENT;GALLERY
			@RequestParam(required = false, defaultValue = "0") Integer from, // 페이지 시작 위치
			@RequestParam(required = false, defaultValue = "10") Integer size, // 페이지 크기
			@RequestParam(required = false) String tag, // 하이라이트의 태그
			@RequestParam(required = false) String styleClass // 하이라이트의 태그 클래스
	) {

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

	// 인기 검색어
	@GetMapping("/popular-words")
	public PopularSearchWordResult searchPopularWords(
			@RequestParam(required = false, defaultValue = "5") Integer size // 크기
	) {

		// 3 주전
		LocalDate threeWeeksAgo = LocalDate.now().minusWeeks(3);

		return searchService.aggregateSearchWord(threeWeeksAgo, size);
	}

}
