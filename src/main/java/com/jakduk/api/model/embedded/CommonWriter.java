package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공통으로 사용하는 작성자
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommonWriter {

	private String userId;
	private String username;
	private Constants.ACCOUNT_TYPE providerId;
	private UserPictureInfo picture;

}
