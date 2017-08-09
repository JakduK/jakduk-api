package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.annotation.SecuredAnonymousUser;
import com.jakduk.api.common.annotation.SecuredUser;
import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistEmailOnEdit;
import com.jakduk.api.common.constraint.ExistUsername;
import com.jakduk.api.common.constraint.ExistUsernameOnEdit;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.security.SnsAuthenticationToken;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.user.*;
import com.jakduk.api.service.UserService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
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

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserService userService;
    @Autowired private RabbitMQPublisher rabbitMQPublisher;
    @Autowired private UserDetailsService userDetailsService;


    @ApiOperation("이메일 기반 회원 가입")
    @SecuredAnonymousUser
    @PostMapping("")
    public EmptyJsonResponse addJakdukUser(
            @ApiParam(value = "회원 폼", required = true) @Valid @RequestBody UserForm form,
            Locale locale) {

        User user = userService.addJakdukUser(form.getEmail(), form.getUsername(), passwordEncoder.encode(form.getPassword().trim()),
                form.getFootballClub(), form.getAbout(), form.getUserPictureId());

        // Perform the security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        form.getPassword()
                )
        );

        AuthUtils.setAuthentication(authentication);

        rabbitMQPublisher.sendWelcome(locale, user.getUsername(), user.getEmail());

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("SNS 기반 회원 가입")
    @PostMapping("/social")
    public EmptyJsonResponse addSocialUser(
            @ApiParam(value = "SNS 회원 폼", required = true) @Valid @RequestBody SocialUserForm form,
            Locale locale,
            HttpSession session) {

        AttemptSocialUser attemptSocialUser = (AttemptSocialUser) session.getAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        if (Objects.isNull(attemptSocialUser))
            throw new ServiceException(ServiceError.CANNOT_GET_ATTEMPT_SNS_PROFILE);

        String largePictureUrl = null;

        if (StringUtils.isNotBlank(form.getExternalLargePictureUrl()))
            largePictureUrl = StringUtils.defaultIfBlank(form.getExternalLargePictureUrl(), null);

        User user = userService.addSocialUser(form.getEmail(), form.getUsername(), attemptSocialUser.getProviderId(),
                attemptSocialUser.getProviderUserId(), form.getFootballClub(), form.getAbout(), form.getUserPictureId(),
                largePictureUrl);

        // Perform the security
        Authentication authentication = authenticationManager.authenticate(
                new SnsAuthenticationToken(
                        user.getEmail()
                )
        );

        session.removeAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        AuthUtils.setAuthentication(authentication);

        rabbitMQPublisher.sendWelcome(locale, user.getUsername(), user.getEmail());

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 중복 검사")
    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public EmptyJsonResponse existEmail(@NotEmpty @Email @ExistEmail @RequestParam String email) {

        userService.existEmail(StringUtils.trim(email));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "회원 프로필 편집 시 Email 중복 검사")
    @SecuredUser
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
                    JakdukUtils.getResourceBundleMessage("ValidationMessages", "validation.msg.email.exists"));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "회원 프로필 편집 시 별명 중복 검사")
    @SecuredUser
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
                    JakdukUtils.getResourceBundleMessage("ValidationMessages", "validation.msg.username.exists"));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "별명 중복 검사")
    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public EmptyJsonResponse existUsername(@NotEmpty @ExistUsername @RequestParam String username) {

        userService.existUsername(StringUtils.trim(username));

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("내 프로필 정보 보기")
    @SecuredUser
    @GetMapping("/profile/me")
    public UserProfileResponse getProfileMe() {

        String language = JakdukUtils.getLanguageCode();

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        return userService.getProfileMe(language, authUserProfile.getId());
    }

    @ApiOperation(value = "내 프로필 정보 편집")
    @SecuredUser
    @PutMapping("/profile/me")
    public EmptyJsonResponse editProfileMe(
            @Valid @RequestBody UserProfileEditForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        User user = userService.editUserProfile(authUserProfile.getId(), form.getEmail(), form.getUsername(), form.getFootballClub(),
                form.getAbout(), form.getUserPictureId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        if (AuthUtils.isJakdukUser()) {
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                    userDetails, authentication.getCredentials(), authentication.getAuthorities()
            );

            AuthUtils.setAuthentication(newAuthentication);
        } else if (AuthUtils.isSnsUser()) {
            Authentication newAuthentication = new SnsAuthenticationToken(userDetails, authentication.getAuthorities());
            AuthUtils.setAuthentication(newAuthentication);
        } else {
            // 참고 @{link http://websystique.com/spring-security/spring-security-4-logout-example/}
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

            throw new ServiceException(ServiceError.INVALID_ACCOUNT);
        }

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 기반 회원의 비밀번호 변경")
    @SecuredUser
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public EmptyJsonResponse editPassword(@Valid @RequestBody UserPasswordForm form) {

        if (! AuthUtils.isJakdukUser())
            throw new ServiceException(ServiceError.FORBIDDEN);

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

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
            return userService.uploadUserPicture(contentType, file.getSize(), file.getBytes());

        } catch (IOException e) {
            throw new ServiceException(ServiceError.IO_EXCEPTION, e);
        }
    }

    @ApiOperation("회원 탈퇴")
    @SecuredUser
    @DeleteMapping("")
    public EmptyJsonResponse deleteUser(
            HttpServletRequest request,
            HttpServletResponse response) {

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        userService.deleteUser(authUserProfile.getId());

        // 참고 @{link http://websystique.com/spring-security/spring-security-4-logout-example/}
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return EmptyJsonResponse.newInstance();
    }

}
