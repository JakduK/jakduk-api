package com.jakduk.api.restcontroller;

import com.jakduk.api.common.util.JwtTokenUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.JakdukDetailsService;
import com.jakduk.api.configuration.authentication.user.JakdukUserDetails;
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
import java.util.Optional;

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

        userService.updateLastLogged(userDetails.getId());

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

    @ApiOperation("SNS 기반 로그인 (존재 하지 않는 회원이면 신규가입 진행)")
    @PostMapping("/login/social/{providerId}")
    public EmptyJsonResponse loginSocialUser(
            @ApiParam(value = "Provider ID", required = true) @PathVariable String providerId,
            @ApiParam(value = "SNS 회원 폼", required = true) @Valid @RequestBody LoginSocialUserForm form,
            Device device,
            HttpServletResponse response) {

        log.info("accessToken={}", form.getAccessToken());

        CoreConst.ACCOUNT_TYPE convertProviderId = CoreConst.ACCOUNT_TYPE.valueOf(providerId.toUpperCase());
        SocialProfile socialProfile = null;
        AttemptSocialUser attemptSocialUser = null;

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
            String token = userService.loginSnsUser(device, socialProfile.getEmail(), oUser.get());

            userService.updateLastLogged(oUser.get().getId());

            response.setHeader(tokenHeader, token);

            return EmptyJsonResponse.newInstance();
        }
        // 그냥 신규 가입
        attemptSocialUser = AttemptSocialUser.builder()
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
