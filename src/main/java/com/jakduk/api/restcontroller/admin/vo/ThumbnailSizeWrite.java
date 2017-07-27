package com.jakduk.api.restcontroller.admin.vo;

import javax.validation.constraints.Min;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 2.
 * @desc     :
 */

public class ThumbnailSizeWrite {
	
	@Min(1)
	private int width = 1;
	
	@Min(1)
	private int height = 1;
	
	private String galleryId;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}

	@Override
	public String toString() {
		return "ThumbnailSizeWrite [width=" + width + ", height=" + height
				+ ", galleryId=" + galleryId + "]";
	}

}
