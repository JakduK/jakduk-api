package com.jakduk.api.model.embedded;

/**
 * Created by pyohwanjang on 2017. 2. 27..
 */

public class UserPictureInfo {

	private String id;
	private String smallPictureUrl;
	private String largePictureUrl;

	public UserPictureInfo() {
	}

	public UserPictureInfo(String id, String smallPictureUrl, String largePictureUrl) {
		this.id = id;
		this.smallPictureUrl = smallPictureUrl;
		this.largePictureUrl = largePictureUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSmallPictureUrl() {
		return smallPictureUrl;
	}

	public void setSmallPictureUrl(String smallPictureUrl) {
		this.smallPictureUrl = smallPictureUrl;
	}

	public String getLargePictureUrl() {
		return largePictureUrl;
	}

	public void setLargePictureUrl(String largePictureUrl) {
		this.largePictureUrl = largePictureUrl;
	}
}
