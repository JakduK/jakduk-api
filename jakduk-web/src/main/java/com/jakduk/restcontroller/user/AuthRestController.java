package com.jakduk.restcontroller.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.daum.connect.DaumConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.jakduk.common.CommonConst;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.model.db.User;
import com.jakduk.model.etc.AuthUserProfile;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.restcontroller.EmptyJsonResponse;
import com.jakduk.restcontroller.user.vo.LoginSocialUserForm;
import com.jakduk.restcontroller.user.vo.UserProfileForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

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

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @ApiOperation(value = "SNS 기반 로그인", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/login/social/{providerId}", method = RequestMethod.POST)
    public EmptyJsonResponse loginSocialUser(
            @PathVariable String providerId,
            @Valid @RequestBody LoginSocialUserForm form,
            NativeWebRequest request) {

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
        if (!userIds.isEmpty()) {
            userService.signInSocialUser(existUser);

            return EmptyJsonResponse.newInstance();
        }

        // SNS 신규 가입.
        ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection);
        sessionStrategy.setAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE, signInAttempt);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    @ApiOperation(value = "SNS 기반 회원 가입시 필요한 회원 프로필 정보", produces = "application/json", response = UserProfileForm.class)
    @RequestMapping(value = "/social/attempted", method = RequestMethod.GET)
    public UserProfileForm loginSocialUser(
                NativeWebRequest request) {

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

        if (Objects.isNull(connection))
            throw new ServiceException(ServiceError.CANNOT_GET_SNS_PROFILE);

        ConnectionKey connectionKey = connection.getKey();

        CommonConst.ACCOUNT_TYPE convertProviderId = CommonConst.ACCOUNT_TYPE.valueOf(connectionKey.getProviderId().toUpperCase());
        UserProfile existUser = userService.findUserProfileByProviderIdAndProviderUserId(convertProviderId, connectionKey.getProviderUserId());
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

        UserProfileForm user = new UserProfileForm();
        user.setEmail(socialProfile.getEmail());
        user.setUsername(username);

        if (Objects.nonNull(existUser)) {
            user.setId(existUser.getId());
            user.setAbout(existUser.getAbout());

            if (Objects.nonNull(existUser.getSupportFC()))
                user.setFootballClub(existUser.getSupportFC().getId());
        }

        return user;
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
