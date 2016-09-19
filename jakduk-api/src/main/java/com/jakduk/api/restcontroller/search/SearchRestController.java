package com.jakduk.api.restcontroller.search;

import com.jakduk.api.restcontroller.EmptyJsonResponse;
import com.jakduk.api.restcontroller.search.vo.SearchResultResponse;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import com.jakduk.core.service.SearchService;
import io.searchbox.core.SearchResult;
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

import javax.validation.Valid;
import java.util.Map;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Slf4j
@Api(tags = "찾기", description = "찾기 API")
@Validated
@RestController
@RequestMapping("/api/search")
public class SearchRestController {
	
	@Autowired
	private SearchService searchService;

	@ApiOperation(value = "찾기", response = SearchResultResponse.class)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public SearchResultResponse getSearch(
			@ApiParam(value = "검색어") @Valid @NotEmpty @RequestParam String q,
			@ApiParam(value = "PO;CO;GA") @Valid @NotEmpty @RequestParam String w,
			@ApiParam(value = "페이지 시작 위치") @RequestParam(required = false, defaultValue = "0") Integer from,
			@ApiParam(value = "페이지 크기")@RequestParam(required = false, defaultValue = "10") Integer size) {

		log.debug("q=" + q + ", w=" + w + ", from=" + from + ", size=" + size);

		if (size <= 0) size = 10;

		SearchResultResponse response = new SearchResultResponse();

		if (StringUtils.contains(w, CommonConst.SEARCH_TYPE.PO.name())) {
			SearchResult result = searchService.searchDocumentBoard(q, from, size);

			if (result.isSucceeded()) {
				response.setPosts(result.getJsonObject());
			}
		}

		//Map<String, Object> result = searchService.getSearch(q, w, from, size);

		return response;
	}
}
