package com.jakduk.api.restcontroller.vo.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(description = "통합 찾기 응답 객체")
public class SearchUnifiedResponse {

	@ApiModelProperty(value = "매칭된 글 객체")
	private SearchArticleResult articleResult;

	@ApiModelProperty(value = "매칭된 댓글 객체")
	private SearchCommentResult commentResult;

	@ApiModelProperty(value = "매칭된 사진 객체")
	private SearchGalleryResult galleryResult;

}
