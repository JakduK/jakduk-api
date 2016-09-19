package com.jakduk.api.restcontroller.user.vo;

import com.jakduk.core.common.constraints.ExistEmail;
import com.jakduk.core.common.constraints.ExistUsername;
import com.jakduk.core.common.constraints.FieldMatch;
import com.jakduk.core.exception.FormValidationErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author pyohwan
 * 16. 6. 26 오후 1:12
 */

@Getter
@ApiModel(description = "이메일 기반 회원 가입 폼")
@FieldMatch(first = "password", second = "passwordConfirm", message = FormValidationErrorCode.PASSWORD_MISMATCH)
public class UserForm {

    @ApiModelProperty(required = true, example = "example@jakduk.com")
    @NotEmpty(message = FormValidationErrorCode.EMAIL_NOT_EMPTY)
    @Size(min = 6, max=30, message = FormValidationErrorCode.EMAIL_SIZE)
    @Email(message = FormValidationErrorCode.EMAIL_NOT_FORMAT)
    @ExistEmail
    private String email;

    @ApiModelProperty(required = true, example = "JakdukUser")
    @NotEmpty(message = FormValidationErrorCode.USERNAME_NOT_EMPTY)
    @Size(min = 2, max=20, message = FormValidationErrorCode.USERNAME_SIZE)
    @ExistUsername
    private String username;

    @ApiModelProperty(required = true, example = "password01")
    @NotEmpty(message = FormValidationErrorCode.PASSWORD_NOT_EMPTY)
    @Size(min = 4, max=20, message = FormValidationErrorCode.PASSWORD_SIZE)
    private String password;

    @ApiModelProperty(required = true, example = "password01")
    @NotEmpty(message = FormValidationErrorCode.PASSWORD_CONFIRM_NOT_EMPTY)
    @Size(min = 4, max=20, message = FormValidationErrorCode.PASSWORD_CONFIRM_SIZE)
    private String passwordConfirm;

    @ApiModelProperty(value = "축구단(FootballClub) ID")
    private String footballClub;

    @ApiModelProperty(example = "안녕하세요.", value = "자기 소개")
    private String about;
}
