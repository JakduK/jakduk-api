package com.jakduk.api.restcontroller.search.vo;

import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SearchResultData {
	private String posts;
	private String comments;
	private Map<String, BoardFreeOnSearchComment> postsHavingComments;
	private String galleries;
}
