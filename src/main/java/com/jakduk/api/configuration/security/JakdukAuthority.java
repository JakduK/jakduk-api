package com.jakduk.api.configuration.security;

import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;

import java.util.Arrays;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 6.
 * @desc     : 
 */

public enum JakdukAuthority {
	ROLE_ANONYMOUS(1), // 미가입
	ROLE_USER_01(10), // 이메일 미인증 회원
	ROLE_USER_02(11), // 이메일 인증 회원
	ROLE_USER_03(12),
	ROLE_ROOT(90);

	private Integer code;

	JakdukAuthority(Integer code) {
		this.code = code;
	}

	static public JakdukAuthority findByCode(Integer code) {
		return Arrays.stream(JakdukAuthority.values())
			.filter(commonRole -> commonRole.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new ServiceException(ServiceError.ILLEGAL_ARGUMENT));
	}

	static public Boolean isAdminAuthority(String authority) {
		JakdukAuthority jakdukAuthority = JakdukAuthority.valueOf(authority);
		switch (jakdukAuthority) {
			case ROLE_ROOT:
				return true;
			default:
				return false;
		}
	}

	static public Boolean isUserAuthority(String authority) {
		JakdukAuthority jakdukAuthority = JakdukAuthority.valueOf(authority);
		switch (jakdukAuthority) {
			case ROLE_USER_01:
			case ROLE_USER_02:
			case ROLE_USER_03:
				return true;
			default:
				return false;
		}
	}

	public Integer getCode() {
		return code;
	}
}
