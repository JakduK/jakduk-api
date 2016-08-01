package com.jakduk.api.restcontroller.user;

import com.jakduk.api.common.util.JwtTokenUtil;
import com.jakduk.api.common.vo.AttemptSocialUser;
import com.jakduk.api.restcontroller.EmptyJsonResponse;
import com.jakduk.api.restcontroller.user.vo.*;
import com.jakduk.core.authentication.common.CommonPrincipal;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.exception.DuplicateDataException;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.FootballService;
import com.jakduk.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
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
    private PasswordEncoder encoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FootballService footballService;

    @Value("${jwt.token.header}")
    private String tokenHeader;

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
    public EmptyJsonResponse addSocialUser(@RequestHeader(value = "x-attempt-token") String attemptedToken,
                                           @Valid @RequestBody UserProfileForm form,
                                           Device device,
                                           HttpServletResponse response) {

        if (! jwtTokenUtil.isValidateToken(attemptedToken))
            throw new ServiceException(ServiceError.EXPIRATION_TOKEN);

        AttemptSocialUser attemptSocialUser = jwtTokenUtil.getAttemptedFromToken(attemptedToken);

        User user = User.builder()
                .email(form.getEmail().trim())
                .username(form.getUsername().trim())
                .providerId(attemptSocialUser.getProviderId())
                .providerUserId(attemptSocialUser.getProviderUserId())
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

        if (Objects.nonNull(about) && !about.isEmpty())
            user.setAbout(form.getAbout().trim());

        userService.save(user);

        log.debug("social user created. user=" + user);

        String token = jwtTokenUtil.generateToken(new CommonPrincipal(user), device);

        response.setHeader(tokenHeader, token);

        return EmptyJsonResponse.newInstance();

    }

    @ApiOperation(value = "회원 프로필 편집 시 Email 중복 검사", produces = "application/json")
    @RequestMapping(value = "/exist/email/edit", method = RequestMethod.GET)
    public Boolean existEmailOnEdit(@RequestParam() String email) {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile userProfile = userService.findByNEIdAndEmail(commonPrincipal.getId(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "비로그인 상태에서 특정 user Id를 제외하고 Email 중복 검사", produces = "application/json")
    @RequestMapping(value = "/exist/email/anonymous", method = RequestMethod.GET)
    public Boolean existEmailOnAnonymous(@RequestParam() String email,
                                         @RequestParam() String id) {

        UserProfile userProfile = userService.findByNEIdAndEmail(id.trim(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "회원 프로필 편집 시 별명 중복 검사", produces = "application/json")
    @RequestMapping(value = "/exist/username/edit", method = RequestMethod.GET)
    public Boolean existUsernameOnEdit(@RequestParam() String username) {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile userProfile = userService.findByNEIdAndUsername(commonPrincipal.getId().trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "비 로그인 상태에서 특정 user Id를 제외하고 별명 중복 검사", produces = "application/json")
    @RequestMapping(value = "/exist/username/anonymous", method = RequestMethod.GET)
    public Boolean existUsernameOnAnonymous(@RequestParam() String username,
                                            @RequestParam() String id) {

        UserProfile userProfile = userService.findByNEIdAndUsername(id.trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "이메일 중복 검사", produces = "application/json")
    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public Boolean existEmail(@RequestParam String email) {

        if (Objects.isNull(email))
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        return userService.existEmail(email.trim());
    }

    @ApiOperation(value = "별명 중복 검사", produces = "application/json")
    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public Boolean existUsername(@RequestParam String username) {

        if (Objects.isNull(username))
            throw new ServiceException(ServiceError.INVALID_PARAMETER);

        return userService.existUsernameOnWrite(username.trim());
    }

    @ApiOperation(value = "내 프로필 정보 보기", produces = "application/json", response = UserProfileResponse.class)
    @RequestMapping(value = "/profile/me", method = RequestMethod.GET)
    public UserProfileResponse getProfileMe() {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        String language = commonService.getLanguageCode(LocaleContextHolder.getLocale(), null);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile user = userService.findUserProfileById(commonPrincipal.getId());

        UserProfileResponse response = new UserProfileResponse();
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setAbout(user.getAbout());
        response.setProviderId(user.getProviderId());

        FootballClub footballClub = user.getSupportFC();

        LocalName localName = footballService.getLocalNameOfFootballClub(footballClub, language);

        if (Objects.nonNull(localName)) response.setFootballClubName(localName);

        return response;
    }

    @ApiOperation(value = "내 프로필 정보 편집", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/profile/me", method = RequestMethod.PUT)
    public EmptyJsonResponse editProfileMe(@Valid @RequestBody UserProfileOnEditForm form) {

        if (! commonService.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        String email = form.getEmail();
        String username = form.getUsername();
        String footballClub = form.getFootballClub();
        String about = form.getAbout();

        if (Objects.nonNull(email)) {
            email = email.trim();
        }
        if (Objects.nonNull(username)) {
            username = username.trim();
        }
        if (Objects.nonNull(footballClub)) {
            footballClub = footballClub.trim();
        }
        if (Objects.nonNull(about)) {
            about = about.trim();
        }

        User user = userService.findById(commonPrincipal.getId());

        if (Objects.nonNull(email) && ! email.isEmpty())
            user.setEmail(email);

        if (Objects.nonNull(username) && ! username.isEmpty())
            user.setUsername(username);

        if (Objects.nonNull(footballClub) && ! footballClub.isEmpty()) {
            FootballClub supportFC = footballService.findById(footballClub);
            user.setSupportFC(supportFC);
        }

        if (Objects.nonNull(about) && ! about.isEmpty())
            user.setAbout(about);

        userService.save(user);

        log.debug("user updated. user=" + user);

        if (commonService.isJakdukUser()) {
            userService.signInJakdukUser(user);
        } else if (commonService.isSocialUser()) {
            userService.signInSocialUser(user);
        }

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 기반 회원의 비밀번호 변경", produces = "application/json", response = EmptyJsonResponse.class)
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public EmptyJsonResponse editPassword(@Valid @RequestBody UserPasswordForm form) {

        if (! commonService.isJakdukUser())
            throw new ServiceException(ServiceError.FORBIDDEN);

        userService.updateUserPassword(form.getNewPassword());

        return EmptyJsonResponse.newInstance();
    }

}
