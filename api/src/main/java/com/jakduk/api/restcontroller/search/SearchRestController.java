package com.jakduk.api.restcontroller.search;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.core.model.vo.SearchUnifiedResponse;
import com.jakduk.core.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private SearchService searchService;

	@ApiOperation(value = "찾기")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public SearchUnifiedResponse searchUnified(
			@ApiParam(value = "검색어", required = true) @NotEmpty @RequestParam String q,
			@ApiParam(value = "PO;CO;GA", required = true) @NotEmpty @RequestParam(defaultValue = "PO;CO;GA") String w,
			@ApiParam(value = "페이지 시작 위치") @RequestParam(required = false, defaultValue = "0") Integer from,
			@ApiParam(value = "페이지 크기")@RequestParam(required = false, defaultValue = "10") Integer size) {

		log.debug("q=" + q + ", w=" + w + ", from=" + from + ", size=" + size);

		if (size <= 0) size = 10;

		searchService.indexDocumentSearchWord(StringUtils.lowerCase(q), UserUtils.getCommonWriter());

		return searchService.searchUnified(q, w, from, size);
	}
}
