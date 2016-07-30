package com.jakduk.restcontroller.search.vo;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

import com.jakduk.model.simple.BoardFreeOnSearchComment;

@Data
@Builder
public class SearchResultData {
	private String posts;
	private String comments;
	private Map<String, BoardFreeOnSearchComment> postsHavingComments;
	private String galleries;
}
