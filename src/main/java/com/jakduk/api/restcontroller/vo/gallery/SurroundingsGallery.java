package com.jakduk.api.restcontroller.vo.gallery;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */

public class SurroundingsGallery {
	private String id; // 사진 ID
	private String name; // 사진 이름
	private String imageUrl; // 사진 풀 URL
	private String thumbnailUrl; // 사진 썸네일 URL

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
}
