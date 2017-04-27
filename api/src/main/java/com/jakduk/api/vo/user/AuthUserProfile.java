package com.jakduk.api.vo.user;

import com.jakduk.core.model.embedded.UserPictureInfo;
import com.jakduk.core.common.CoreConst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 인증된 회원의 프로필 정보를 담는 객체
 *
 * @author pyohwan
 *         16. 7. 14 오전 12:21
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthUserProfile {

    private String id;
    private String email;
    private String username;
    private CoreConst.ACCOUNT_TYPE providerId;
    private List<String> roles;
    private UserPictureInfo picture;

}
