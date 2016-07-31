package com.jakduk.common.vo;

import com.jakduk.common.CommonConst;
import lombok.*;

/**
 * @author pyohwan
 *         16. 7. 30 오후 9:54
 */

@Getter
@Setter
public class AttemptSocialUser {
    private String email;
    private String username;
    private CommonConst.ACCOUNT_TYPE providerId;
    private String providerUserId;
}
