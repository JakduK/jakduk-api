package com.jakduk.api.restcontroller.vo.user;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * SNS 기반 로그인 폼
 *
 * @author pyohwan
 *         16. 7. 9 오후 11:48
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class LoginSocialUserForm {
    @NotEmpty
    private String accessToken; // OAuth 인증에서 사용하는 AccessToken
}
