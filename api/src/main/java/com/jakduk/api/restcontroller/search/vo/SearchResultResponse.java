package com.jakduk.api.restcontroller.search.vo;

import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import com.jakduk.core.model.vo.SearchCommentResult;
import com.jakduk.core.model.vo.SearchPostResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@ApiModel(description = "회원 프로필 응답 객체")
@Getter
@Setter
public class SearchResultResponse {

	@ApiModelProperty(value = "검색된 게시물")
	private SearchPostResult postResult;

	@ApiModelProperty(value = "검색된 댓글")
	private SearchCommentResult commentResult;

	@ApiModelProperty(value = "검색된 사진첩")
	private Map<String, Object> galleries;

	@ApiModelProperty(value = "검색된 댓글의 게시물 정보")
	private Map<String, BoardFreeOnSearchComment> postsHavingComments;
}
