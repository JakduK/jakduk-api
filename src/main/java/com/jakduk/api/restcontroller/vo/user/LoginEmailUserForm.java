package com.jakduk.api.restcontroller.vo.user;

import lombok.Getter;

/**
 * 이메일 기반 로그인 폼
 *
 * @author pyohwan
 *         16. 7. 28 오후 10:17
 */

@Getter
public class LoginEmailUserForm {
    private String username;
    private String password;
}
