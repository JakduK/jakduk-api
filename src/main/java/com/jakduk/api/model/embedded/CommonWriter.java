package com.jakduk.api.model.embedded;

import com.jakduk.api.common.CoreConst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통으로 사용하는 작성자
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommonWriter {
	
	private String userId;
	private String username;
	private CoreConst.ACCOUNT_TYPE providerId;
	private UserPictureInfo picture;

}
