package com.jakduk.api.restcontroller;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistEmailOnEdit;
import com.jakduk.api.common.constraint.ExistUsername;
import com.jakduk.api.common.constraint.ExistUsernameOnEdit;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.SnsAuthenticationToken;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.service.UserService;
import com.jakduk.api.vo.user.*;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserPicture;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;

/**
 * @author pyohawan
 * 16. 4. 5 오전 12:17
 */

@Api(tags = "User", description = "회원 API")
@RestController
@RequestMapping("/api/user")
@Validated
public class UserRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "이메일 기반 회원 가입")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public EmptyJsonResponse addJakdukUser(@Valid @RequestBody UserForm form,
                                           Locale locale,
                                           HttpSession session) {

        User user = userService.addJakdukUser(form.getEmail(), form.getUsername(), passwordEncoder.encode(form.getPassword().trim()),
                form.getFootballClub(), form.getAbout(), form.getUserPictureId());

        emailService.sendWelcome(locale, form.getUsername().trim(), form.getEmail().trim());

        // Perform the authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getUsername(),
                        form.getPassword()
                )
        );

        ApiUtils.login(session, authentication);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("SNS 기반 회원 가입")
    @PostMapping("/social")
    public EmptyJsonResponse addSocialUser(
            @ApiParam(value = "SNS 회원 폼", required = true) @Valid @RequestBody SocialUserForm form,
            Locale locale,
            HttpSession session) {

        AttemptSocialUser attemptSocialUser = (AttemptSocialUser) session.getAttribute(ApiConst.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        String largePictureUrl = null;

        if (StringUtils.isNotBlank(form.getExternalLargePictureUrl()))
            largePictureUrl = StringUtils.defaultIfBlank(form.getExternalLargePictureUrl(), null);

        User user = userService.addSocialUser(form.getEmail(), form.getUsername(), attemptSocialUser.getProviderId(),
                attemptSocialUser.getProviderUserId(), form.getFootballClub(), form.getAbout(), form.getUserPictureId(),
                largePictureUrl);

        emailService.sendWelcome(locale, user.getUsername(), user.getEmail());

        // Perform the authentication
        Authentication authentication = authenticationManager.authenticate(
                new SnsAuthenticationToken(
                        user.getEmail()
                )
        );

        ApiUtils.login(session, authentication);

        session.removeAttribute(ApiConst.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 중복 검사")
    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public EmptyJsonResponse existEmail(@NotEmpty @Email @ExistEmail @RequestParam String email) {

        userService.existEmail(StringUtils.trim(email));

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

        UserProfile userProfile = userService.findByNEIdAndEmail(StringUtils.trim(id), StringUtils.trim(email));

        if (! ObjectUtils.isEmpty(userProfile))
            throw new ServiceException(ServiceError.FORM_VALIDATION_FAILED,
                    CoreUtils.getResourceBundleMessage("ValidationMessages", "validation.msg.email.exists"));

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

        UserProfile userProfile = userService.findByNEIdAndUsername(StringUtils.trim(id), StringUtils.trim(username));

        if (! ObjectUtils.isEmpty(userProfile))
            throw new ServiceException(ServiceError.FORM_VALIDATION_FAILED,
                    CoreUtils.getResourceBundleMessage("ValidationMessages", "validation.msg.username.exists"));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "별명 중복 검사")
    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public EmptyJsonResponse existUsername(@NotEmpty @ExistUsername @RequestParam String username) {

        userService.existUsername(StringUtils.trim(username));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("내 프로필 정보 보기")
    @GetMapping("/profile/me")
    public UserProfileResponse getProfileMe(Locale locale) {

        String language = CoreUtils.getLanguageCode(locale, null);

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        return userService.getProfileMe(language, authUserProfile.getId());
    }

    @ApiOperation(value = "내 프로필 정보 편집")
    @RequestMapping(value = "/profile/me", method = RequestMethod.PUT)
    public EmptyJsonResponse editProfileMe(@Valid @RequestBody UserProfileEditForm form) {

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        userService.editUserProfile(authUserProfile.getId(), form.getEmail(), form.getUsername(), form.getFootballClub(),
                form.getAbout(), form.getUserPictureId());

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 기반 회원의 비밀번호 변경")
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public EmptyJsonResponse editPassword(@Valid @RequestBody UserPasswordForm form) {

        if (! UserUtils.isJakdukUser())
            throw new ServiceException(ServiceError.FORBIDDEN);

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        userService.updateUserPassword(authUserProfile.getId(), passwordEncoder.encode(form.getNewPassword().trim()));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "프로필 사진 올리기")
    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public UserPicture uploadUserPicture(@RequestParam MultipartFile file) {

        String contentType = file.getContentType();

        if (! StringUtils.startsWithIgnoreCase(contentType, "image/"))
            throw new ServiceException(ServiceError.FILE_ONLY_IMAGE_TYPE_CAN_BE_UPLOADED);

        try {
            UserPicture userPicture = userService.uploadUserPicture(contentType, file.getSize(), file.getBytes());

            return userPicture;

        } catch (IOException e) {
            throw new ServiceException(ServiceError.IO_EXCEPTION, e);
        }
    }

}
