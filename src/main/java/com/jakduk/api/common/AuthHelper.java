package com.jakduk.api.common;

import com.jakduk.api.configuration.security.UserDetailsImpl;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

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
