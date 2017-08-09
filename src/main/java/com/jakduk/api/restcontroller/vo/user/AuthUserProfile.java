package com.jakduk.api.restcontroller.vo.user;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.UserPictureInfo;
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
    private Constants.ACCOUNT_TYPE providerId;
    private List<String> roles;
    private UserPictureInfo picture;

}
