package com.jakduk.api.restcontroller.search;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.service.search.SearchService;
import com.jakduk.core.model.vo.PopularSearchWordResult;
import com.jakduk.core.model.vo.SearchUnifiedResponse;
import com.jakduk.core.service.CommonSearchService;
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
import java.time.ZoneId;

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
	
	@Autowired
	private CommonSearchService commonSearchService;

	@Autowired
	private SearchService searchService;

	@ApiOperation(value = "찾기")
	@GetMapping(path = "")
	public SearchUnifiedResponse searchUnified(
			@ApiParam(value = "검색어", required = true) @NotEmpty @RequestParam String q,
			@ApiParam(value = "PO;CO;GA", required = true) @NotEmpty @RequestParam(defaultValue = "PO;CO;GA") String w,
			@ApiParam(value = "페이지 시작 위치") @RequestParam(required = false, defaultValue = "0") Integer from,
			@ApiParam(value = "페이지 크기")@RequestParam(required = false, defaultValue = "10") Integer size) {

		log.debug("q={}, w={}, from={}, size={}", q, w, from, size);

		if (size <= 0) size = 10;

		SearchUnifiedResponse searchUnifiedResponse = searchService.searchUnified(q, w, from, size);

		commonSearchService.indexDocumentSearchWord(StringUtils.lowerCase(q), UserUtils.getCommonWriter());

		return searchUnifiedResponse;
	}

	@ApiOperation(value = "인기 검색어")
	@GetMapping(path = "/popular/words")
	public PopularSearchWordResult searchPopularWords(
			@ApiParam(value = "크기") @RequestParam(required = false, defaultValue = "5") Integer size) {

		// 한달전
		Long registerDateFrom = LocalDate.now().minusMonths(1L).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

		return searchService.aggregateSearchWord(registerDateFrom, size);
	}
}
