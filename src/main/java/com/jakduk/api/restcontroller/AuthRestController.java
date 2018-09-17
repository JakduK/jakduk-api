package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.configuration.security.SnsAuthenticationToken;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.User;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.user.AttemptSocialUser;
import com.jakduk.api.restcontroller.vo.user.LoginSocialUserForm;
import com.jakduk.api.restcontroller.vo.user.SessionUser;
import com.jakduk.api.restcontroller.vo.user.SocialProfile;
import com.jakduk.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * 인증 API
 *
 * @author pyohwan
 * 16. 6. 29 오전 12:27
 */

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired private AuthUtils authUtils;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserService userService;

    // SNS 기반 로그인 (존재 하지 않는 회원이면 신규가입 진행)
    @PostMapping("/login/{providerId}")
    public EmptyJsonResponse loginSnsUser(
            @PathVariable String providerId,
            @Valid @RequestBody LoginSocialUserForm form,
            HttpSession session) {

        Constants.ACCOUNT_TYPE cvtProviderId = Constants.ACCOUNT_TYPE.valueOf(providerId.toUpperCase());
        SocialProfile socialProfile = null;

        switch (cvtProviderId) {
            case DAUM:
                socialProfile = authUtils.getDaumProfile(form.getAccessToken());
                break;
            case FACEBOOK:
                socialProfile = authUtils.getFacebookProfile(form.getAccessToken());
                break;
        }

        log.info("loginSnsUser form={}, socialProfile={}", form, socialProfile);

        Optional<User> optUser = userService.findOneByProviderIdAndProviderUserId(cvtProviderId, socialProfile.getId());

        // 가입 회원이라 로그인
        if (optUser.isPresent()) {
            User user = optUser.get();
            userService.checkBackwardCompatibilityOfSnsUser(socialProfile.getEmail(), user);

            // Perform the security
            Authentication authentication = authenticationManager.authenticate(
                    new SnsAuthenticationToken(user.getEmail())
            );

            AuthUtils.setAuthentication(authentication);

            return EmptyJsonResponse.newInstance();
        }

        // 그냥 신규 가입으로 프로필을 세션에 임시 저장한다.
        AttemptSocialUser attemptSocialUser = new AttemptSocialUser();
        attemptSocialUser.setUsername(socialProfile.getNickname());
        attemptSocialUser.setProviderId(cvtProviderId);
        attemptSocialUser.setProviderUserId(socialProfile.getId());

        if (StringUtils.isNotBlank(socialProfile.getEmail()))
            attemptSocialUser.setEmail(socialProfile.getEmail());

        if (StringUtils.isNotBlank(socialProfile.getLargePictureUrl()))
            attemptSocialUser.setExternalLargePictureUrl(socialProfile.getLargePictureUrl());

        if (StringUtils.isNotBlank(socialProfile.getSmallPictureUrl()))
            attemptSocialUser.setExternalSmallPictureUrl(socialProfile.getSmallPictureUrl());

        session.setAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE, attemptSocialUser);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    // SNS 기반 회원 가입시 필요한 회원 프로필 정보
    @GetMapping("/user/attempt")
    public AttemptSocialUser getAttemptSocialUser(HttpSession session) {

        AttemptSocialUser attemptSocialUser = (AttemptSocialUser) session.getAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        if (Objects.isNull(attemptSocialUser))
            throw new ServiceException(ServiceError.CANNOT_GET_ATTEMPT_SNS_PROFILE);

        return attemptSocialUser;
    }

    // 세션에 있는 나의 프로필 정보
    @GetMapping("/user")
    public SessionUser getMySessionProfile() {

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        if (Objects.isNull(sessionUser))
            throw new ServiceException(ServiceError.ANONYMOUS);

        return sessionUser;
    }

}
