package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistUsername;
import com.jakduk.api.common.constraint.FieldMatch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author pyohwan
 * 16. 6. 26 오후 1:12
 */

@ApiModel(description = "이메일 기반 회원 가입 폼")
@FieldMatch(first = "password", second = "passwordConfirm", message = "{validation.msg.password.mismatch}")
@Getter
@Builder
public class UserForm {

    @ApiModelProperty(required = true, example = "example@jakduk.com")
    @Size(min = 6, max=30)
    @NotEmpty
    @Email
    @ExistEmail
    private String email;

    @ApiModelProperty(required = true, example = "JakdukUser")
    @Size(min = 2, max=20)
    @NotEmpty
    @ExistUsername
    private String username;

    @ApiModelProperty(required = true, example = "password01")
    @Size(min = 4, max=20)
    @NotEmpty
    private String password;

    @ApiModelProperty(required = true, example = "password01")
    @Size(min = 4, max=20)
    @NotEmpty
    private String passwordConfirm;

    @ApiModelProperty(example = "54e1d2c68bf86df3fe819874", value = "축구단(FootballClub) ID")
    private String footballClub;

    @ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
    private String about;

    @ApiModelProperty(example = "58ad9b35a0c73a045d45979a", value = "UserPicture의 ID")
    private String userPictureId;
}
