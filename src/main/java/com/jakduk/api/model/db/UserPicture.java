package com.jakduk.api.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwan on 17. 2. 16.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class UserPicture {

	@Id
	private String id;
	private Constants.GALLERY_STATUS_TYPE status;
	private String contentType;

}
