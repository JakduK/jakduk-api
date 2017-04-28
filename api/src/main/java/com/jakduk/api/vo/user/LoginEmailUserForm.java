package com.jakduk.api.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author pyohwan
 *         16. 7. 28 오후 10:17
 */

@Getter
@ApiModel(description = "이메일 기반 로그인 폼")
public class LoginEmailUserForm implements Serializable {

    @ApiModelProperty(required = true, example = "test05@test.com")
    private String username;

    @ApiModelProperty(required = true, example = "1111")
    private String password;

}
