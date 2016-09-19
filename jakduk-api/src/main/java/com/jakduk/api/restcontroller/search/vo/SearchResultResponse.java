package com.jakduk.api.restcontroller.search.vo;

import com.google.gson.JsonObject;
import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@ApiModel(description = "회원 프로필 응답 객체")
@Getter
@Setter
public class SearchResultResponse {

	@ApiModelProperty(value = "검색된 게시물")
	private JsonObject posts;

	@ApiModelProperty(value = "검색된 댓글")
	private String comments;

	@ApiModelProperty(value = "검색된 사진첩")
	private String galleries;

	@ApiModelProperty(value = "검색된 댓글의 게시물 정보")
	private Map<String, BoardFreeOnSearchComment> postsHavingComments;
}
