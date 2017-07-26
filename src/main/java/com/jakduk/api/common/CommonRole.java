package com.jakduk.api.common;

import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 6.
 * @desc     : 
 */

@Getter
public enum CommonRole {

	ROLE_USER_01(10), // 미인증 회원
	ROLE_USER_02(11), // 인증 회원
	ROLE_USER_03(12),
	ROLE_ADMIN(30),
	ROLE_ROOT(90);

	private Integer code;

	CommonRole(Integer code) {
		this.code = code;
	}

	static public CommonRole findByCode(Integer code) {
		return Arrays.stream(CommonRole.values())
				.filter(commonRole -> commonRole.code.equals(code))
				.findFirst()
				.orElseThrow(() -> new ServiceException(ServiceError.ILLEGAL_ARGUMENT));
	}

}
