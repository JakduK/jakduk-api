package com.jakduk.api.common;

import com.jakduk.api.configuration.security.UserDetailsImpl;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    /**
     * 컨트롤러에서 파라미터로 넘어오면 미인증의 경우 authentication이 null 일 경우가 있다.
     * @param authentication
     * @return
     */
    @Deprecated
    public CommonWriter getCommonWriter(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetail = (UserDetailsImpl) authentication.getPrincipal();

            return CommonWriter.builder()
                    .userId(userDetail.getId())
                    .username(userDetail.getNickname())
                    .providerId(userDetail.getProviderId())
                    .picture(userDetail.getPicture())
                    .build();
        }

        return null;
    }

}
