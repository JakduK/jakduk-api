package com.jakduk.restcontroller.user;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.common.JakdukPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.configuration.authentication.JakdukDetailsService;
import com.jakduk.configuration.authentication.JwtTokenUtil;
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
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.daum.connect.DaumConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

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
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    @Autowired
    private DaumConnectionFactory daumConnectionFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JakdukDetailsService jakdukDetailsService;

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
        JakdukPrincipal userDetails = (JakdukPrincipal) jakdukDetailsService.loadUserByUsername(form.getUsername());

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
                                             @ApiIgnore NativeWebRequest request,
                                             HttpServletResponse response) {

        CommonConst.ACCOUNT_TYPE convertProviderId = CommonConst.ACCOUNT_TYPE.valueOf(providerId.toUpperCase());

        AccessGrant accessGrant = new AccessGrant(form.getAccessToken());
        Connection<?> connection = null;

        switch (convertProviderId) {
            case FACEBOOK:
                connection = facebookConnectionFactory.createConnection(accessGrant);
                break;
            case DAUM:
                connection = daumConnectionFactory.createConnection(accessGrant);
                break;
        }

        assert connection != null;
        ConnectionKey connectionKey = connection.getKey();

        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(providerId, new HashSet<>(Collections.singletonList(connectionKey.getProviderUserId())));
        User existUser = userService.findOneByProviderIdAndProviderUserId(convertProviderId, connectionKey.getProviderUserId());

        // 로그인 처리.
        if (! ObjectUtils.isEmpty(userIds)) {
            userService.signInSocialUser(existUser);

            return EmptyJsonResponse.newInstance();
        }

        // SNS 신규 가입.
        org.springframework.social.connect.UserProfile socialProfile = connection.fetchUserProfile();

        String username = null;
        if (Objects.nonNull(socialProfile.getName())) {
            username = socialProfile.getName();
        } else if (Objects.nonNull(socialProfile.getUsername())) {
            username = socialProfile.getUsername();
        } else {
            if (Objects.nonNull(socialProfile.getFirstName())) {
                username = socialProfile.getFirstName();
            }
            if (Objects.nonNull(socialProfile.getLastName())) {
                username = Objects.isNull(username) ? socialProfile.getLastName() : ' ' + socialProfile.getLastName();
            }
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", socialProfile.getEmail());
        claims.put("username", username);

        // connections 컬렉션엔 있지만 user 컬렉션에 없는 경우는 spring-social 이전에 가입한 회원이다.
        if (Objects.nonNull(existUser)) {
            claims.put("id", existUser.getId());
            claims.put("about", existUser.getAbout());

            if (Objects.nonNull(existUser.getSupportFC()))
                claims.put("footballClub", existUser.getSupportFC().getId());
        }

        String attemptedToken = jwtTokenUtil.generateAttemptedToken(claims);
        response.setHeader(attemptedTokenHeader, attemptedToken);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    @ApiOperation(value = "SNS 기반 회원 가입시 필요한 회원 프로필 정보", produces = "application/json")
    @RequestMapping(value = "/social/attempted", method = RequestMethod.GET)
    public Map<String, Object> getSocialAttemptedUser(@RequestHeader(value = "x-attempted-token") String token) {

        return jwtTokenUtil.getAttemptedFromToken(token);
    }

    @ApiOperation(value = "로그인 중인 내 프로필", produces = "application/json", response = AuthUserProfile.class)
    @RequestMapping(value = "/auth/user", method = RequestMethod.GET)
    public AuthUserProfile getMyProfile() {

        AuthUserProfile authUserProfile = commonService.getAuthUserProfile();

        if (Objects.isNull(authUserProfile))
            throw new NoSuchElementException(commonService.getResourceBundleMessage("messages.common", "common.exception.no.such.element"));

        return authUserProfile;
    }
}
