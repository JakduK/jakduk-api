package com.jakduk.api.restcontroller;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.SnsAuthenticationToken;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.service.UserService;
import com.jakduk.api.vo.user.*;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author pyohwan
 * 16. 6. 29 오전 12:27
 */

@Slf4j
@Api(tags = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @ApiOperation("이메일 기반 로그인")
    @PostMapping("/login")
    public EmptyJsonResponse loginJakdukUser(
            @ApiParam(value = "이메일 회원 폼", required = true) @Valid @RequestBody LoginEmailUserForm form,
            HttpSession session) {

        // Perform the authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(),
                        form.getPassword()
                )
        );

        ApiUtils.login(session, authentication);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("SNS 기반 로그인 (존재 하지 않는 회원이면 신규가입 진행)")
    @PostMapping("/login/{providerId}")
    public EmptyJsonResponse loginSNSUser(
            @ApiParam(value = "Provider ID", required = true) @PathVariable String providerId,
            @ApiParam(value = "SNS 회원 폼", required = true) @Valid @RequestBody LoginSocialUserForm form,
            HttpSession session) {

        CoreConst.ACCOUNT_TYPE convertProviderId = CoreConst.ACCOUNT_TYPE.valueOf(providerId.toUpperCase());
        SocialProfile socialProfile = null;

        switch (convertProviderId) {
            case DAUM:
                socialProfile = userUtils.getDaumProfile(form.getAccessToken());
                break;
            case FACEBOOK:
                socialProfile = userUtils.getFacebookProfile(form.getAccessToken());
                break;
        }

        log.info("socialProfile providerId:{} providerUserId:{} nickname:{} email:{}",
                convertProviderId.name(), socialProfile.getId(), socialProfile.getNickname(), socialProfile.getEmail());

        Optional<User> oUser = userService.findOneByProviderIdAndProviderUserId(convertProviderId, socialProfile.getId());

        // 가입 회원이라 로그인
        if (oUser.isPresent()) {
            userService.checkBackwardCompatibilityOfSnsUser(socialProfile.getEmail(), oUser.get());

            // Perform the authentication
            Authentication authentication = authenticationManager.authenticate(
                    new SnsAuthenticationToken(
                            oUser.get().getEmail()
                    )
            );

            ApiUtils.login(session, authentication);

            return EmptyJsonResponse.newInstance();
        }

        // 그냥 신규 가입으로 프로필을 세션에 임시 저장한다.
        AttemptSocialUser attemptSocialUser = AttemptSocialUser.builder()
                .username(socialProfile.getNickname())
                .providerId(convertProviderId)
                .providerUserId(socialProfile.getId())
                .build();

        if (StringUtils.isNotBlank(socialProfile.getEmail()))
            attemptSocialUser.setEmail(socialProfile.getEmail());

        if (StringUtils.isNotBlank(socialProfile.getLargePictureUrl()))
            attemptSocialUser.setExternalLargePictureUrl(socialProfile.getLargePictureUrl());

        if (StringUtils.isNotBlank(socialProfile.getSmallPictureUrl()))
            attemptSocialUser.setExternalSmallPictureUrl(socialProfile.getSmallPictureUrl());

        session.setAttribute(ApiConst.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE, attemptSocialUser);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    @ApiOperation("SNS 기반 회원 가입시 필요한 회원 프로필 정보")
    @GetMapping("/user/attempt")
    public AttemptSocialUser getSocialAttemptedUser(HttpSession session) {

        return (AttemptSocialUser) session.getAttribute(ApiConst.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);
    }

    @ApiOperation(value = "세션에 있는 나의 프로필 정보")
    @GetMapping("/user")
    public AuthUserProfile getMyProfile() {

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        if (ObjectUtils.isEmpty(authUserProfile))
            throw new ServiceException(ServiceError.ANONYMOUS);

        return authUserProfile;
    }

}
