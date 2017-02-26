package com.jakduk.api.common.vo;

import com.jakduk.core.common.CoreConst;
import lombok.*;

/**
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
