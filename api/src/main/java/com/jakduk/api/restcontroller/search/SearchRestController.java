package com.jakduk.api.restcontroller.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.restcontroller.search.vo.SearchResultResponse;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.BoardDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.elasticsearch.CommentOnES;
import com.jakduk.core.service.SearchService;
import io.searchbox.core.SearchResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Slf4j
@Api(tags = "Search", description = "찾기 API")
@Validated
@RestController
@RequestMapping("/api/search")
public class SearchRestController {
	
	@Autowired
	private SearchService searchService;

	@Autowired
	private BoardDAO boardDAO;

	@ApiOperation(value = "찾기", response = SearchResultResponse.class)
	@RequestMapping(value = "", method = RequestMethod.GET)
	public SearchResultResponse getSearch(
			@ApiParam(value = "검색어", required = true) @NotEmpty @RequestParam String q,
			@ApiParam(value = "PO;CO;GA", required = true) @NotEmpty @RequestParam String w,
			@ApiParam(value = "페이지 시작 위치") @RequestParam(required = false, defaultValue = "0") Integer from,
			@ApiParam(value = "페이지 크기")@RequestParam(required = false, defaultValue = "10") Integer size) {

		log.debug("q=" + q + ", w=" + w + ", from=" + from + ", size=" + size);

		if (size <= 0) size = 10;

		ObjectMapper objectMapper = new ObjectMapper();
		SearchResultResponse response = new SearchResultResponse();

		try {
			if (StringUtils.contains(w, CoreConst.SEARCH_TYPE.PO.name())) {
				SearchResult result = searchService.searchDocumentBoard(q, from, size);

				if (result.isSucceeded())
					response.setPosts(objectMapper.readValue(result.getJsonString(), Map.class));
			}

			if (StringUtils.contains(w, CoreConst.SEARCH_TYPE.CO.name())) {
				List<ObjectId> ids = new ArrayList<>();
				SearchResult result = searchService.searchDocumentComment(q, from, size);

				if (result.isSucceeded()) {
					List<SearchResult.Hit<CommentOnES, Void>> hits = result.getHits(CommentOnES.class);
					hits.forEach(hit -> {
						String id = hit.source.getBoardItem().getId();
						ids.add(new ObjectId(id));
					});

					response.setComments(objectMapper.readValue(result.getJsonString(), Map.class));
					response.setPostsHavingComments(boardDAO.getBoardFreeOnSearchComment(ids));
				}
			}

			if (StringUtils.contains(w, CoreConst.SEARCH_TYPE.GA.name())) {
				int tempSize = size;

				if (size < 10)
					tempSize = 4;

				SearchResult result = searchService.searchDocumentGallery(q, from, tempSize);

				if (result.isSucceeded())
					response.setGalleries(objectMapper.readValue(result.getJsonString(), Map.class));

			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException(ServiceError.IO_EXCEPTION);
		}

		return response;
	}
}
