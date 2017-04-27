package com.jakduk.api.vo.user;

import com.jakduk.core.common.CoreConst;
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
    private CoreConst.ACCOUNT_TYPE providerId;
    private String providerUserId;
    private String externalSmallPictureUrl;
    private String externalLargePictureUrl;

}
