package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.simple.BoardFreeSimple;
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
@ApiModel(description = "사진 정보 응답 객체")
public class GalleryResponse {

	@ApiModelProperty(value = "사진 정보")
	private GalleryDetail gallery;

	@ApiModelProperty(value = "해당 사진의 앞, 뒤 사진 목록")
	private List<SurroundingsGallery> surroundingsGalleries;

	@ApiModelProperty(value = "이 사진을 사용하는 게시물 목록")
	private List<BoardFreeSimple> linkedPosts;
}
