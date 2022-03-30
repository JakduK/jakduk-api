package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;

/**
 * Created by pyohwanjang on 2017. 4. 3..
 */

public class GalleryDetail {

	private String id; // 사진 ID
	private CommonWriter writer; // 올린이
	private String name; // 사진 이름
	private String imageUrl; // 사진 풀 URL
	private String thumbnailUrl; // 사진 썸네일 URL
	private GalleryStatus status; // 사진 상태

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public GalleryStatus getStatus() {
		return status;
	}

	public void setStatus(GalleryStatus status) {
		this.status = status;
	}
}
