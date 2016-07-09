package com.jakduk.restcontroller;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.restcontroller.vo.UserProfileForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author pyohwan
 * 16. 6. 29 오전 12:27
 */

@Slf4j
@Api(value = "Auth", description = "인증 API")
@CrossOrigin
@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private FacebookConnectionFactory facebookConnectionFactory;

    @Autowired
    private DaumConnectionFactory daumConnectionFactory;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @ApiOperation(value = "Social 로그인", response = CommonPrincipal.class)
    @RequestMapping(value = "/login/social/{providerId}", method = RequestMethod.POST)
    public CommonPrincipal loginSocialUser(
            @PathVariable Optional<String> providerId,
            @RequestParam Optional<String> accessToken,
            NativeWebRequest request) {

        if (!accessToken.isPresent())
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        if (!providerId.isPresent())
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        CommonConst.ACCOUNT_TYPE convertProviderId = CommonConst.ACCOUNT_TYPE.valueOf(providerId.get().toUpperCase());

        AccessGrant accessGrant = new AccessGrant(accessToken.get());
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

        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(providerId.get(), new HashSet<>(Collections.singletonList(connectionKey.getProviderUserId())));
        User existUser = userService.findOneByProviderIdAndProviderUserId(convertProviderId, connectionKey.getProviderUserId());

        // 로그인 처리.
        if (!userIds.isEmpty()) {
            userService.signInSocialUser(existUser);

            CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

            return commonPrincipal;
        }

        // SNS 신규 가입.
        ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection);
        sessionStrategy.setAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE, signInAttempt);

        throw new ServiceException(ServiceError.NOT_REGISTER_WITH_SNS);
    }

    @ApiOperation(value = "Social 가입을 위한 프로필 정보", response = UserProfileForm.class)
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

        UserProfileForm user = new UserProfileForm();
        user.setEmail(socialProfile.getEmail());
        user.setUsername(socialProfile.getName());

        if (Objects.nonNull(existUser)) {
            user.setId(existUser.getId());
            user.setAbout(existUser.getAbout());

            if (Objects.nonNull(existUser.getSupportFC()))
                user.setFootballClub(existUser.getSupportFC().getId());
        }

        return user;
    }
}
