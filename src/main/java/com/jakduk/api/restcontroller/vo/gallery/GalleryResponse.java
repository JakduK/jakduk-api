package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.simple.ArticleSimple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GalleryResponse {
	private GalleryDetail gallery; // 사진 정보
	private List<SurroundingsGallery> surroundingsGalleries; // 해당 사진의 앞, 뒤 사진 목록
	private List<ArticleSimple> linkedPosts; // 이 사진을 사용하는 게시물 목록
}
