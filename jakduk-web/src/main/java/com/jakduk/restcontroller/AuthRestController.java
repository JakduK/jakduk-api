package com.jakduk.restcontroller;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.User;
import com.jakduk.model.web.user.UserProfileForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by pyohwan on 16. 6. 29.
 */

@Slf4j
@Api(value = "Auth", description = "인증 API")
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
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @ApiOperation(value = "Social 로그인")
    @RequestMapping(value = "/login/facebook", method = RequestMethod.POST)
    public ResponseEntity loginSocialUser(
                @RequestParam String accessToken,
                NativeWebRequest request) {

        Locale locale = localeResolver.resolveLocale((HttpServletRequest) request.getNativeRequest());

        if (Objects.isNull(accessToken))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));

        AccessGrant accessGrant = new AccessGrant(accessToken);
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);

        ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection);
        sessionStrategy.setAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE, signInAttempt);

        org.springframework.social.connect.UserProfile socialProfile = connection.fetchUserProfile();
        //FacebookTemplate facebookTemplate = new FacebookTemplate(accessToken);
        //org.springframework.social.facebook.api.User facebookUser = facebookTemplate.userOperations().getUserProfile();

        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo("facebook", new HashSet<>(Arrays.asList(socialProfile.getId())));

        // Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
        User existUser = userService.findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE.FACEBOOK, socialProfile.getId());

        if (Objects.nonNull(userIds)) {
            userService.signInSocialUser(existUser);

            CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

            return new ResponseEntity<>(commonPrincipal, HttpStatus.OK);
        }

        UserProfileForm user = new UserProfileForm();
        user.setEmail(socialProfile.getEmail());
        user.setUsername(socialProfile.getName());

        if (Objects.nonNull(existUser)) {
            user.setId(existUser.getId());
            user.setAbout(existUser.getAbout());

            if (Objects.nonNull(existUser.getSupportFC()))
                user.setFootballClub(existUser.getSupportFC().getId());
        }

        log.debug("user=" + user);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Social 로그인")
    @RequestMapping(value = "/login/facebook", method = RequestMethod.GET)
    public ResponseEntity loginSocialUser(
                NativeWebRequest request) {

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        ConnectionKey connectionKey = connection.getKey();

        log.debug("user=" + connectionKey);

        return new ResponseEntity<>(connectionKey, HttpStatus.ACCEPTED);
    }
}
