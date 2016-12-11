package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CoreConst;
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
	private CoreConst.ACCOUNT_TYPE providerId;
}
