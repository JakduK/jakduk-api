package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.constraint.ExistEmail;
import com.jakduk.api.common.constraint.ExistUsername;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.security.SnsAuthenticationToken;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
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
    @Autowired private UserService userService;
    @Autowired private RabbitMQPublisher rabbitMQPublisher;
    @Autowired private UserDetailsService userDetailsService;

    @ApiOperation("이메일 기반 회원 가입")
    @PostMapping("")
    public EmptyJsonResponse createJakdukUser(
            @ApiParam(value = "회원 폼", required = true) @Valid @RequestBody UserForm form) {

        String password = form.getPassword().trim();

        User user = userService.createJakdukUser(form.getEmail().trim(), form.getUsername().trim(), password,
                form.getFootballClub(), form.getAbout(), form.getUserPictureId());

        String email = user.getEmail();

        // Perform the security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        AuthUtils.setAuthentication(authentication);

        rabbitMQPublisher.sendWelcome(JakdukUtils.getLocale(), user.getUsername(), email);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("SNS 기반 회원 가입")
    @PostMapping("/social")
    public EmptyJsonResponse createSocialUser(
            @ApiParam(value = "SNS 회원 폼", required = true) @Valid @RequestBody SocialUserForm form,
            HttpSession session) {

        AttemptSocialUser attemptSocialUser = (AttemptSocialUser) session.getAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        if (Objects.isNull(attemptSocialUser))
            throw new ServiceException(ServiceError.CANNOT_GET_ATTEMPT_SNS_PROFILE);

        String largePictureUrl = StringUtils.defaultIfBlank(form.getExternalLargePictureUrl(), null);

        User user = userService.createSocialUser(form.getEmail().trim(), form.getUsername().trim(), attemptSocialUser.getProviderId(),
                attemptSocialUser.getProviderUserId().trim(), form.getFootballClub(), form.getAbout(),
                form.getUserPictureId(), largePictureUrl);

        // Perform the security
        Authentication authentication = authenticationManager.authenticate(
                new SnsAuthenticationToken(
                        user.getEmail()
                )
        );

        AuthUtils.setAuthentication(authentication);

        session.removeAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE);

        rabbitMQPublisher.sendWelcome(JakdukUtils.getLocale(), user.getUsername(), user.getEmail());

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "이메일 중복 검사")
    @GetMapping("/exist/email")
    public EmptyJsonResponse existEmail(@NotEmpty @Email @ExistEmail @RequestParam String email) {
        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "별명 중복 검사")
    @GetMapping("/exist/username")
    public EmptyJsonResponse existUsername(@NotEmpty @ExistUsername @RequestParam String username) {
        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation("내 프로필 정보 보기")
    @GetMapping("/profile/me")
    public UserProfileResponse getProfileMe() {

        String language = JakdukUtils.getLanguageCode();

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        return userService.getProfileMe(language, sessionUser.getId());
    }

    @ApiOperation(value = "내 프로필 정보 편집")
    @PutMapping("/profile/me")
    public EmptyJsonResponse editProfileMe(
            @Valid @RequestBody UserProfileEditForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        User user = userService.editUserProfile(sessionUser.getId(), form.getEmail().trim(), form.getUsername().trim(),
                form.getFootballClub(), form.getAbout(), form.getUserPictureId());

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

    @ApiOperation(value = "프로필 사진 올리기")
    @PostMapping("/picture")
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

    @ApiOperation(value = "이메일 기반 회원의 비밀번호 변경")
    @PutMapping("/password")
    public EmptyJsonResponse editPassword(@Valid @RequestBody UserPasswordForm form) {

        if (! AuthUtils.isJakdukUser())
            throw new ServiceException(ServiceError.FORBIDDEN);

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        userService.updateUserPassword(sessionUser.getId(), form.getNewPassword().trim());

        return EmptyJsonResponse.newInstance();
    }

    // 비밀번호 찾기. 이메일 기반 회원일 경우 메일을 발송한다.
    @PostMapping("/password/find")
    public UserPasswordFindResponse findPassword(
            @Valid @RequestBody UserPasswordFindForm form) {

        return userService.sendEmailToResetPassword(form.getEmail().trim(), form.getCallbackUrl().trim());
    }

    // 비밀번호 재설정 처리.
    @PostMapping("/password/reset")
    public UserPasswordFindResponse resetPassword(
            @Valid @RequestBody UserPasswordResetForm form) {

        return userService.resetPasswordWithToken(form.getCode().trim(), form.getPassword().trim());
    }

    @ApiOperation("회원 탈퇴")
    @DeleteMapping("")
    public EmptyJsonResponse deleteUser(
            HttpServletRequest request,
            HttpServletResponse response) {

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        userService.deleteUser(sessionUser.getId());

        // 참고 @{link http://websystique.com/spring-security/spring-security-4-logout-example/}
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return EmptyJsonResponse.newInstance();
    }

}
