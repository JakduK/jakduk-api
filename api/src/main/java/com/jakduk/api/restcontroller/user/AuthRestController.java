package com.jakduk.api.restcontroller.user;

import com.jakduk.api.common.util.JwtTokenUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.common.vo.AttemptSocialUser;
import com.jakduk.api.common.vo.AuthUserProfile;
import com.jakduk.api.common.vo.SocialProfile;
import com.jakduk.api.configuration.authentication.JakdukDetailsService;
import com.jakduk.api.configuration.authentication.SocialDetailService;
import com.jakduk.api.configuration.authentication.user.JakdukUserDetails;
import com.jakduk.api.configuration.authentication.user.SocialUserDetails;
import com.jakduk.api.restcontroller.user.vo.LoginEmailUserForm;
import com.jakduk.api.restcontroller.user.vo.LoginSocialUserForm;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import com.jakduk.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author pyohwan
 * 16. 6. 29 오전 12:27
 */

@Slf4j
@Api(tags = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.token.attempted.header}")
    private String attemptedTokenHeader;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JakdukDetailsService jakdukDetailsService;

    @Autowired
    private SocialDetailService socialDetailService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "이메일 기반 로그인")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public EmptyJsonResponse loginSocialUser(@RequestBody LoginEmailUserForm form,
                                             @ApiIgnore Device device,
                                             HttpServletResponse response) {

        // Perform the authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(),
                        form.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-authentication so we can generate token
        JakdukUserDetails userDetails = (JakdukUserDetails) jakdukDetailsService.loadUserByUsername(form.getUsername());

        String token = jwtTokenUtils.generateToken(device, userDetails.getId(), userDetails.getUsername(), userDetails.getNickname(),
                userDetails.getProviderId().name());

        response.setHeader(tokenHeader, token);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "JWT 토큰 갱신")
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public EmptyJsonResponse refreshAndGetAuthenticationToken(HttpServletRequest request,
                                                              HttpServletResponse response) {

        String token = request.getHeader(tokenHeader);

        if (jwtTokenUtils.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtils.refreshToken(token);
            response.setHeader(tokenHeader, refreshedToken);

            return EmptyJsonResponse.newInstance();
        } else {
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);
        }
    }

    @ApiOperation(value = "SNS 기반 로그인 (존재 하지 않는 회원이면 신규가입 진행)")
    @RequestMapping(value = "/login/social/{providerId}", method = RequestMethod.POST)
    public EmptyJsonResponse loginSocialUser(@PathVariable String providerId,
                                             @Valid @RequestBody LoginSocialUserForm form,
                                             Device device,
                                             HttpServletResponse response) {

        log.info("accessToken={}", form.getAccessToken());

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

        log.info("socialProfile({}, {}) email({})", socialProfile.getId(), socialProfile.getNickname(), socialProfile.getEmail());

        try {
            User user = userService.findOneByProviderIdAndProviderUserId(convertProviderId, socialProfile.getId());

            // 과거에 SNS 가입 회원들은 email이 없는 경우가 있음. 이메일을 DB에 저장
            if (StringUtils.isBlank(user.getEmail()) && StringUtils.isNotBlank(socialProfile.getEmail())) {
                user.setEmail(socialProfile.getEmail());
                userService.save(user);

                log.info("user({},{}) email({}) has been entered.", user.getId(), user.getUsername(), user.getEmail());
            }

            // 토큰 생성
            SocialUserDetails userDetails = (SocialUserDetails) socialDetailService.loadUserByUsername(user.getId());

            String token = jwtTokenUtils.generateToken(device, userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(),
                    userDetails.getProviderId().name());

            response.setHeader(tokenHeader, token);

            return EmptyJsonResponse.newInstance();

        } catch (ServiceException ignored) {
        }

        // 신규 가입.
        AttemptSocialUser attemptSocialUser = AttemptSocialUser.builder()
                .username(socialProfile.getNickname())
                .providerId(convertProviderId)
                .providerUserId(socialProfile.getId())
                .build();

        // Daum은 이메일을 안 알려준다.
        if (StringUtils.isNotBlank(socialProfile.getEmail()))
            attemptSocialUser.setEmail(socialProfile.getEmail());

        if (StringUtils.isNotBlank(socialProfile.getLargePictureUrl()))
            attemptSocialUser.setExternalLargePictureUrl(socialProfile.getLargePictureUrl());

        if (StringUtils.isNotBlank(socialProfile.getSmallPictureUrl()))
            attemptSocialUser.setExternalSmallPictureUrl(socialProfile.getSmallPictureUrl());

        String attemptedToken = jwtTokenUtils.generateAttemptedToken(attemptSocialUser);

        response.setHeader(attemptedTokenHeader, attemptedToken);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    @ApiOperation(value = "SNS 기반 회원 가입시 필요한 회원 프로필 정보")
    @RequestMapping(value = "/social/attempt", method = RequestMethod.GET)
    public AttemptSocialUser getSocialAttemptedUser(@RequestHeader(value = "x-attempt-token") String attemptedToken) {

        if (! jwtTokenUtils.isValidateToken(attemptedToken))
            throw new ServiceException(ServiceError.EXPIRATION_TOKEN);

        return jwtTokenUtils.getAttemptedFromToken(attemptedToken);
    }

    @ApiOperation(value = "JWT 토큰 속 프로필 정보")
    @GetMapping("/auth/user")
    public AuthUserProfile getMyProfile() {

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        if (ObjectUtils.isEmpty(authUserProfile))
            throw new ServiceException(ServiceError.ANONYMOUS);

        return authUserProfile;
    }
}
