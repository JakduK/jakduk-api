package com.jakduk.api.restcontroller.user;

import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistEmailOnEdit;
import com.jakduk.api.common.constraint.ExistUsername;
import com.jakduk.api.common.constraint.ExistUsernameOnEdit;
import com.jakduk.api.common.util.JwtTokenUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.common.vo.AttemptSocialUser;
import com.jakduk.api.configuration.authentication.user.CommonPrincipal;
import com.jakduk.api.configuration.authentication.user.JakdukUserDetail;
import com.jakduk.api.restcontroller.EmptyJsonResponse;
import com.jakduk.api.restcontroller.user.vo.*;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.DuplicateDataException;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.EmailService;
import com.jakduk.core.service.FootballService;
import com.jakduk.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;

/**
 * @author pyohawan
 * 16. 4. 5 오전 12:17
 */

@Api(tags = "User", description = "회원 API")
@RestController
@RequestMapping("/api/user")
@Validated
public class UserRestController {

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private FootballService footballService;

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "이메일 기반 회원 가입")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public EmptyJsonResponse addJakdukUser(@Valid @RequestBody UserForm form,
                                           Device device,
                                           Locale locale,
                                           HttpServletResponse response) {

        User user = userService.addJakdukUser(form.getEmail(), form.getUsername(), passwordEncoder.encode(form.getPassword().trim()),
                form.getFootballClub(), form.getAbout());

        emailService.sendWelcome(locale, form.getUsername().trim(), form.getEmail().trim());

        JakdukUserDetail userDetails = userUtils.signInJakdukUser(user);

        String token = jwtTokenUtils.generateToken(new CommonPrincipal(userDetails), device);

        response.setHeader(tokenHeader, token);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "SNS 기반 회원 가입")
    @RequestMapping(value = "/social", method = RequestMethod.POST)
    public EmptyJsonResponse addSocialUser(@RequestHeader(value = "x-attempt-token") String attemptedToken,
                                           @Valid @RequestBody UserProfileForm form,
                                           Device device,
                                           Locale locale,
                                           HttpServletResponse response) {

        if (! jwtTokenUtils.isValidateToken(attemptedToken))
            throw new ServiceException(ServiceError.EXPIRATION_TOKEN);

        AttemptSocialUser attemptSocialUser = jwtTokenUtils.getAttemptedFromToken(attemptedToken);

        User user = userService.addSocialUser(form.getEmail(), form.getUsername(), attemptSocialUser.getProviderId(),
                attemptSocialUser.getProviderUserId(), form.getFootballClub(), form.getAbout());

        emailService.sendWelcome(locale, form.getUsername().trim(), form.getEmail().trim());

        String token = jwtTokenUtils.generateToken(new CommonPrincipal(user), device);

        response.setHeader(tokenHeader, token);

        return EmptyJsonResponse.newInstance();

    }

    @ApiOperation(value = "회원 프로필 편집 시 Email 중복 검사")
    @RequestMapping(value = "/exist/email/edit", method = RequestMethod.GET)
    public EmptyJsonResponse existEmailOnEdit(@NotEmpty @Email @ExistEmailOnEdit @RequestParam String email) {

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "비 로그인 상태에서 특정 user Id를 제외하고 Email 중복 검사")
    @RequestMapping(value = "/exist/email/anonymous", method = RequestMethod.GET)
    public EmptyJsonResponse existEmailOnAnonymous(@NotEmpty @Email @RequestParam String email,
                                                   @NotEmpty @RequestParam String id) {

        UserProfile userProfile = userService.findByNEIdAndEmail(id.trim(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(CoreUtils.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "회원 프로필 편집 시 별명 중복 검사")
    @RequestMapping(value = "/exist/username/edit", method = RequestMethod.GET)
    public EmptyJsonResponse existUsernameOnEdit(@NotEmpty @ExistUsernameOnEdit @RequestParam String username) {

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "비 로그인 상태에서 특정 user Id를 제외하고 별명 중복 검사")
    @RequestMapping(value = "/exist/username/anonymous", method = RequestMethod.GET)
    public EmptyJsonResponse existUsernameOnAnonymous(@NotEmpty @RequestParam String username,
                                                      @NotEmpty @RequestParam String id) {

        UserProfile userProfile = userService.findByNEIdAndUsername(id.trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(CoreUtils.getResourceBundleMessage("messages.user", "user.msg.replicated.data"));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 중복 검사")
    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public EmptyJsonResponse existEmail(@NotEmpty @Email @ExistEmail @RequestParam String email) {

        //userService.existEmail(email.trim());

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "별명 중복 검사")
    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public EmptyJsonResponse existUsername(@NotEmpty @ExistUsername @RequestParam String username) {

        userService.existUsername(username.trim());

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "내 프로필 정보 보기")
    @RequestMapping(value = "/profile/me", method = RequestMethod.GET)
    public UserProfileResponse getProfileMe() {

        String language = CoreUtils.getLanguageCode(LocaleContextHolder.getLocale(), null);

        CommonPrincipal commonPrincipal = UserUtils.getCommonPrincipal();

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

    @ApiOperation(value = "내 프로필 정보 편집")
    @RequestMapping(value = "/profile/me", method = RequestMethod.PUT)
    public EmptyJsonResponse editProfileMe(@Valid @RequestBody UserProfileOnEditForm form) {

        CommonPrincipal commonPrincipal = UserUtils.getCommonPrincipal();

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

        //log.debug("user updated. user=" + user);

        if (UserUtils.isJakdukUser()) {
            userUtils.signInJakdukUser(user);
        } else if (UserUtils.isSocialUser()) {
            userUtils.signInSocialUser(user);
        }

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 기반 회원의 비밀번호 변경")
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public EmptyJsonResponse editPassword(@Valid @RequestBody UserPasswordForm form) {

        if (! UserUtils.isJakdukUser())
            throw new ServiceException(ServiceError.FORBIDDEN);

        CommonPrincipal commonPrincipal = UserUtils.getCommonPrincipal();

        userService.updateUserPassword(commonPrincipal.getId(), passwordEncoder.encode(form.getNewPassword().trim()));

        return EmptyJsonResponse.newInstance();
    }

}
