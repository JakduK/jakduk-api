package com.jakduk.restcontroller.user;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.exception.DuplicateDataException;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.model.etc.AuthUserProfile;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.restcontroller.EmptyJsonResponse;
import com.jakduk.restcontroller.user.vo.UserForm;
import com.jakduk.restcontroller.user.vo.UserProfileForm;
import com.jakduk.restcontroller.user.vo.UserProfileResponse;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import com.jakduk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author pyohawan
 * 16. 4. 5 오전 12:17
 */

@Slf4j
@Api(tags = "회원", description = "회원 관련")
@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private StandardPasswordEncoder encoder;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FootballService footballService;

    @ApiOperation(value = "이메일 기반 회원 가입", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public EmptyJsonResponse addJakdukUser(
            @Valid @RequestBody UserForm form) {

        User user = User.builder()
                .email(form.getEmail().trim())
                .username(form.getUsername().trim())
                .password(encoder.encode(form.getPassword().trim()))
                .providerId(CommonConst.ACCOUNT_TYPE.JAKDUK)
                .build();

        String footballClub = form.getFootballClub();
        String about = form.getAbout();

        if (Objects.nonNull(footballClub) && !footballClub.isEmpty()) {
            FootballClub supportFC = footballService.findById(footballClub);

            user.setSupportFC(supportFC);
        }

        if (Objects.nonNull(about) && !about.isEmpty()) {
            user.setAbout(about.trim());
        }

        ArrayList<Integer> roles = new ArrayList<>();
        roles.add(CommonRole.ROLE_NUMBER_USER_01);

        user.setRoles(roles);

        userService.save(user);

        log.debug("JakduK user created. user=" + user);

        userService.signInJakdukUser(user);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "SNS 기반 회원 가입", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/social", method = RequestMethod.POST)
    public EmptyJsonResponse addSocialUser(
            @Valid @RequestBody UserProfileForm form,
            NativeWebRequest request) {

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

        if (Objects.isNull(connection))
            throw new ServiceException(ServiceError.CANNOT_GET_SNS_PROFILE);

        ConnectionKey connectionKey = connection.getKey();

        CommonConst.ACCOUNT_TYPE providerId = CommonConst.ACCOUNT_TYPE.valueOf(connectionKey.getProviderId().toUpperCase());
        String providerUserId = connectionKey.getProviderUserId();

        User user = User.builder()
                .email(form.getEmail().trim())
                .username(form.getUsername().trim())
                .providerId(providerId)
                .providerUserId(providerUserId)
                .build();

        ArrayList<Integer> roles = new ArrayList<>();
        roles.add(CommonRole.ROLE_NUMBER_USER_01);

        user.setRoles(roles);

        String footballClub = form.getFootballClub();
        String about = form.getAbout();

        if (Objects.nonNull(footballClub) && !footballClub.isEmpty()) {
            FootballClub supportFC = footballService.findById(footballClub);

            user.setSupportFC(supportFC);
        }

        if (Objects.nonNull(about) && !about.isEmpty()) {
            user.setAbout(form.getAbout().trim());
        }

        providerSignInUtils.doPostSignUp(user.getProviderUserId(), request);

        userService.save(user);

        log.debug("social user created. user=" + user);

        userService.signInSocialUser(user);

        return EmptyJsonResponse.newInstance();

    }

    @ApiOperation(value = "회원 프로필 업데이트 시 Email 중복 체크", produces = "application/json")
    @RequestMapping(value = "/exist/email/update", method = RequestMethod.GET)
    public Boolean existEmailOnUpdate(@RequestParam() String email) {

        if (!commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile userProfile = userService.findByNEIdAndEmail(commonPrincipal.getId(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "비로그인 상태(id를 제외한)에서 Email 중복 체크", produces = "application/json")
    @RequestMapping(value = "/exist/email/anonymous", method = RequestMethod.GET)
    public Boolean existEmailOnAnonymous(@RequestParam() String email,
                                         @RequestParam() String id) {

        UserProfile userProfile = userService.findByNEIdAndEmail(id.trim(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "회원 프로필 업데이트 시 별명 중복 체크", produces = "application/json")
    @RequestMapping(value = "/exist/username/update", method = RequestMethod.GET)
    public Boolean existUsernameOnUpdate(@RequestParam() String username) {

        if (!commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile userProfile = userService.findByNEIdAndUsername(commonPrincipal.getId().trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "비 로그인 상태(id를 제외한)에서 별명 중복 체크", produces = "application/json")
    @RequestMapping(value = "/exist/username/anonymous", method = RequestMethod.GET)
    public Boolean existUsernameOnAnonymous(@RequestParam() String username,
                                            @RequestParam() String id) {

        UserProfile userProfile = userService.findByNEIdAndUsername(id.trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "이메일 중복 여부", produces = "application/json")
    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public Boolean existEmail(@RequestParam String email) {

        if (Objects.isNull(email))
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        return userService.existEmail(email.trim());
    }

    @ApiOperation(value = "별명 중복 여부", produces = "application/json")
    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public Boolean existUsername(@RequestParam String username) {

        if (Objects.isNull(username))
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        return userService.existUsernameOnWrite(username.trim());
    }

    @ApiOperation(value = "내(이메일 기반) 프로필", produces = "application/json", response = UserProfileResponse.class)
    @RequestMapping(value = "/profile/me", method = RequestMethod.GET)
    public UserProfileResponse profile() {

        if (!commonService.isJakdukUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        Locale locale = LocaleContextHolder.getLocale();
        String language = commonService.getLanguageCode(locale, null);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile user = userService.findUserProfileById(commonPrincipal.getId());

        UserProfileResponse response = new UserProfileResponse();
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setAbout(user.getAbout());

        FootballClub footballClub = user.getSupportFC();

        LocalName localName = footballService.getLocalNameOfFootballClub(footballClub, language);

        if (Objects.nonNull(localName)) response.setFootballClubName(localName);

        return response;
    }
}
