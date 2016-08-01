package com.jakduk.api.restcontroller.gallery.vo;

import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.simple.BoardFreeOnGallery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel(value = "사진 정보 응답 객체")
public class GalleryResponse {
	@ApiModelProperty(value = "사진 정보")
	private Gallery gallery;

	@ApiModelProperty(value = "이전 사진")
	private Gallery prev;

	@ApiModelProperty(value = "다음 사진")
	private Gallery next;

	@ApiModelProperty(value = "관련 게시물")
	private List<BoardFreeOnGallery> linkedPosts;
}
