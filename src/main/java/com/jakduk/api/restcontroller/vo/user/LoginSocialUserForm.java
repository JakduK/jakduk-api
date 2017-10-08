package com.jakduk.api.restcontroller.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author pyohwan
 *         16. 7. 9 오후 11:48
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ApiModel(description = "SNS 기반 로그인 폼")
public class LoginSocialUserForm {

    @ApiModelProperty(required = true, example = "EAALwXK...", value = "OAuth 인증에서 사용하는 AccessToken")
    @NotEmpty
    private String accessToken;

}
