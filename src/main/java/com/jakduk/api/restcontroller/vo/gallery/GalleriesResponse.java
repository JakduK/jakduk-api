package com.jakduk.api.restcontroller.vo.gallery;

import java.util.List;

/**
 * 사진 목록 응답 객체
 *
 * @author pyohwan
 * 16. 5. 8 오후 11:22
 */

public class GalleriesResponse {
	private List<GalleryOnList> galleries; // 사진 목록

	public GalleriesResponse() {
	}

	public GalleriesResponse(List<GalleryOnList> galleries) {
		this.galleries = galleries;
	}

	public List<GalleryOnList> getGalleries() {
		return galleries;
	}
}
