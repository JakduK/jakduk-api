package com.jakduk.restcontroller.user;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.common.JakdukUserDetail;
import com.jakduk.authentication.common.SocialUserDetail;
import com.jakduk.common.CommonConst;
import com.jakduk.common.util.JwtTokenUtil;
import com.jakduk.common.util.UserUtils;
import com.jakduk.common.vo.AttemptSocialUser;
import com.jakduk.common.vo.SocialProfile;
import com.jakduk.configuration.authentication.JakdukDetailsService;
import com.jakduk.configuration.authentication.SocialDetailService;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.model.db.User;
import com.jakduk.model.etc.AuthUserProfile;
import com.jakduk.restcontroller.EmptyJsonResponse;
import com.jakduk.restcontroller.user.vo.LoginEmailUserForm;
import com.jakduk.restcontroller.user.vo.LoginSocialUserForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 6. 29 오전 12:27
 */

@Slf4j
@Api(tags = "인증", description = "인증 관련")
@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JakdukDetailsService jakdukDetailsService;

    @Autowired
    private SocialDetailService socialDetailService;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.token.attempted.header}")
    private String attemptedTokenHeader;

    @ApiOperation(value = "이메일 기반 로그인", produces = "application/json", response = EmptyJsonResponse.class)
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
        JakdukUserDetail userDetails = (JakdukUserDetail) jakdukDetailsService.loadUserByUsername(form.getUsername());

        String token = jwtTokenUtil.generateToken(new CommonPrincipal(userDetails), device);

        response.setHeader(tokenHeader, token);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "JWT 토큰 갱신", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public EmptyJsonResponse refreshAndGetAuthenticationToken(HttpServletRequest request,
                                                              HttpServletResponse response) {

        String token = request.getHeader(tokenHeader);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            response.setHeader(tokenHeader, refreshedToken);

            return EmptyJsonResponse.newInstance();
        } else {
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);
        }
    }

    @ApiOperation(value = "SNS 기반 로그인", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/login/social/{providerId}", method = RequestMethod.POST)
    public EmptyJsonResponse loginSocialUser(@PathVariable String providerId,
                                             @Valid @RequestBody LoginSocialUserForm form,
                                             Device device,
                                             HttpServletResponse response) {

        CommonConst.ACCOUNT_TYPE convertProviderId = CommonConst.ACCOUNT_TYPE.valueOf(providerId.toUpperCase());
        SocialProfile socialProfile = null;

        switch (convertProviderId) {
            case DAUM:
                socialProfile = userUtils.getDaumProfile(form.getAccessToken());
                break;
            case FACEBOOK:
                socialProfile = userUtils.getFacebookProfile(form.getAccessToken());
                break;
        }

        assert socialProfile != null;
        User existUser = userService.findOneByProviderIdAndProviderUserId(convertProviderId, socialProfile.getId());

        // 로그인 처리.
        if (! ObjectUtils.isEmpty(existUser)) {

            SocialUserDetail userDetails = (SocialUserDetail) socialDetailService.loadUserByUsername(existUser.getEmail());
            String token = jwtTokenUtil.generateToken(new CommonPrincipal(userDetails), device);

            response.setHeader(tokenHeader, token);

            return EmptyJsonResponse.newInstance();
        }

        // 신규 가입.
        AttemptSocialUser attemptSocialUser = new AttemptSocialUser();
        attemptSocialUser.setUsername(socialProfile.getNickname());
        attemptSocialUser.setProviderId(convertProviderId);
        attemptSocialUser.setProviderUserId(socialProfile.getId());

        // Daum은 이메일을 안 알려준다.
        if (! ObjectUtils.isEmpty(socialProfile.getEmail()))
            attemptSocialUser.setEmail(socialProfile.getEmail());

        String attemptedToken = jwtTokenUtil.generateAttemptedToken(attemptSocialUser);

        response.setHeader(attemptedTokenHeader, attemptedToken);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    @ApiOperation(value = "SNS 기반 회원 가입시 필요한 회원 프로필 정보", produces = "application/json", response = AttemptSocialUser.class)
    @RequestMapping(value = "/social/attempt", method = RequestMethod.GET)
    public AttemptSocialUser getSocialAttemptedUser(@RequestHeader(value = "x-attempt-token") String attemptedToken) {

        if (! jwtTokenUtil.isValidateToken(attemptedToken))
            throw new ServiceException(ServiceError.EXPIRATION_TOKEN);

        return jwtTokenUtil.getAttemptedFromToken(attemptedToken);
    }

    @ApiOperation(value = "JWT 토큰 속 프로필 정보", produces = "application/json", response = AuthUserProfile.class)
    @RequestMapping(value = "/auth/user", method = RequestMethod.GET)
    public AuthUserProfile getMyProfile() {

        AuthUserProfile authUserProfile = commonService.getAuthUserProfile();

        if (Objects.isNull(authUserProfile))
            throw new NoSuchElementException(commonService.getResourceBundleMessage("messages.common", "common.exception.no.such.element"));

        return authUserProfile;
    }
}
