package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.common.Constants;

/**
 * Created by pyohwanjang on 2017. 4. 14..
 */

public class LinkedItemForm {
	private String itemId; // 아이템 ID
	private Constants.GALLERY_FROM_TYPE from; // 출처

	public String getItemId() {
		return itemId;
	}

	public Constants.GALLERY_FROM_TYPE getFrom() {
		return from;
	}
}
