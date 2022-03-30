package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 3.
 * @desc     :
 */

public class GalleryStatus {

	private Constants.GALLERY_STATUS_TYPE status;

	public GalleryStatus() {
	}

	public GalleryStatus(Constants.GALLERY_STATUS_TYPE status) {
		this.status = status;
	}

	public Constants.GALLERY_STATUS_TYPE getStatus() {
		return status;
	}

	public void setStatus(Constants.GALLERY_STATUS_TYPE status) {
		this.status = status;
	}
}
