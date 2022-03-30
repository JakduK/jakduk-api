package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;

/**
 * Created by pyohwanjang on 2017. 4. 10..
 */

public class LinkedItem {

	private String id;
	private Constants.GALLERY_FROM_TYPE from;

	public LinkedItem() {
	}

	public LinkedItem(String id, Constants.GALLERY_FROM_TYPE from) {
		this.id = id;
		this.from = from;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Constants.GALLERY_FROM_TYPE getFrom() {
		return from;
	}

	public void setFrom(Constants.GALLERY_FROM_TYPE from) {
		this.from = from;
	}
}
