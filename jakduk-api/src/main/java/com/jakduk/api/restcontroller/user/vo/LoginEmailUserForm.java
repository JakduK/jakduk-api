package com.jakduk.api.restcontroller.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author pyohwan
 *         16. 7. 28 오후 10:17
 */

@Getter
@ApiModel(value = "이메일 기반 로그인 폼")
public class LoginEmailUserForm implements Serializable {

    private String username;
    private String password;
}
