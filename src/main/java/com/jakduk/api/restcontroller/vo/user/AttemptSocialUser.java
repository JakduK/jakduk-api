package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.Constants;
import lombok.*;

/**
 * SNS로 회원 가입시 임시로 회원 정보를 담는 객체
 *
 * @author pyohwan
 *         16. 7. 30 오후 9:54
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AttemptSocialUser {

    private String email;
    private String username;
    private Constants.ACCOUNT_TYPE providerId;
    private String providerUserId;
    private String externalSmallPictureUrl;
    private String externalLargePictureUrl;

}
