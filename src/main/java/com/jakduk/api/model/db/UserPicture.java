package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by pyohwan on 17. 2. 16.
 */

@Document
public class UserPicture {

	@Id
	private String id;
	private Constants.GALLERY_STATUS_TYPE status;
	private String contentType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Constants.GALLERY_STATUS_TYPE getStatus() {
		return status;
	}

	public void setStatus(Constants.GALLERY_STATUS_TYPE status) {
		this.status = status;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
