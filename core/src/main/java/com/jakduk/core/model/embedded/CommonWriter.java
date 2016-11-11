package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CommonConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시판 작성자
 * @author pyohwan
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommonWriter {
	
	private String userId;
	private String username;
	private CommonConst.ACCOUNT_TYPE providerId;
}
