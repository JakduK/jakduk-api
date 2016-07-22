package com.jakduk.restcontroller.gallery.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.jakduk.model.db.Gallery;
import com.jakduk.model.simple.BoardFreeOnGallery;

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
