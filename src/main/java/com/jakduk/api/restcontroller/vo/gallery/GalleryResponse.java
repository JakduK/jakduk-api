package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.simple.ArticleSimple;

import java.util.List;

public class GalleryResponse {
	private GalleryDetail gallery; // 사진 정보
	private List<SurroundingsGallery> surroundingsGalleries; // 해당 사진의 앞, 뒤 사진 목록
	private List<ArticleSimple> linkedPosts; // 이 사진을 사용하는 게시물 목록

	public GalleryResponse() {
	}

	public GalleryResponse(GalleryDetail gallery, List<SurroundingsGallery> surroundingsGalleries, List<ArticleSimple> linkedPosts) {
		this.gallery = gallery;
		this.surroundingsGalleries = surroundingsGalleries;
		this.linkedPosts = linkedPosts;
	}

	public GalleryDetail getGallery() {
		return gallery;
	}

	public List<SurroundingsGallery> getSurroundingsGalleries() {
		return surroundingsGalleries;
	}

	public List<ArticleSimple> getLinkedPosts() {
		return linkedPosts;
	}
}
