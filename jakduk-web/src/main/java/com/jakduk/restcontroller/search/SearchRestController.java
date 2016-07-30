package com.jakduk.restcontroller.search;

import java.util.Map;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jakduk.model.simple.BoardFreeOnSearchComment;
import com.jakduk.restcontroller.search.vo.SearchResultData;
import com.jakduk.service.SearchService;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@RestController
@RequestMapping("/api/search")
@Api(tags = "검색", description = "검색 API")
public class SearchRestController {
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method = RequestMethod.GET)
	public SearchResultData dataBoardList(
			@RequestParam String q,
			@RequestParam(required = false) String w,
			@RequestParam(required = false, defaultValue = "0") int from,
			@RequestParam(required = false, defaultValue = "10") int size) {
		
		Map<String, Object> result = searchService.getDataSearch(q, w, from, size);

		return SearchResultData.builder()
			.posts((String) result.get("posts"))
			.comments((String) result.get("comments"))
			.galleries((String) result.get("galleries"))
			.postsHavingComments((Map<String, BoardFreeOnSearchComment>) result.get("postsHavingComments"))
			.build();
	}
}
