package com.jakduk.restcontroller;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.exception.DuplicateDataException;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.restcontroller.vo.UserForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import com.jakduk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 4. 5.
 */

@Slf4j
@Api(value = "USER", description = "회원 API")
@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private StandardPasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FootballService footballService;

    @ApiOperation(value = "JakduK 회원 가입")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String write(@RequestBody UserForm form,
                        HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(form.getEmail()))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.need.validation.email"));

        if (Objects.isNull(form.getUsername()))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.need.validation.username"));

        String password = form.getPassword();
        String passwordConfirm = form.getPasswordConfirm();

        if (Objects.isNull(password))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.placeholder.password"));

        if (Objects.isNull(passwordConfirm))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.placeholder.password.confirm"));


        if (!password.equals(passwordConfirm))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.password.mismatch"));

        UserProfile existEmail = userService.findOneByEmail(form.getEmail());

        if (Objects.nonNull(existEmail))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.already.email"));

        UserProfile existUsername = userService.findOneByUsername(form.getUsername());

        if (Objects.nonNull(existUsername))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.already.username"));

        User user = User.builder()
                .email(form.getEmail().trim())
                .username(form.getUsername().trim())
                .password(encoder.encode(form.getPassword().trim()))
                .providerId(CommonConst.ACCOUNT_TYPE.JAKDUK)
                .build();

        String footballClub = form.getFootballClub();
        String about = form.getAbout();

        if (Objects.nonNull(footballClub) && footballClub.isEmpty() == false) {
            FootballClub supportFC = footballService.findById(footballClub);

            user.setSupportFC(supportFC);
        }

        if (Objects.nonNull(about) && about.isEmpty() == false) {
            user.setAbout(about.trim());
        }

        ArrayList<Integer> roles = new ArrayList<>();
        roles.add(CommonRole.ROLE_NUMBER_USER_01);

        user.setRoles(roles);

        userService.save(user);

        log.debug("JakduK user created. user=" + user);

        userService.signUpJakdukUser(user);

        return CommonConst.RESPONSE_VOID_OBJECT;
    }

    @ApiOperation(value = "회원 프로필 업데이트 시 Email 중복 체크")
    @RequestMapping(value = "/exist/email/update", method = RequestMethod.GET)
    public Boolean existEmailOnUpdate(@RequestParam(required = true) String email,
                                       HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        if (Objects.isNull(commonPrincipal.getId()))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        UserProfile userProfile = userService.findByNEIdAndEmail(commonPrincipal.getId(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "비로그인 상태(id를 제외한)에서 Email 중복 체크")
    @RequestMapping(value = "/exist/email/anonymous", method = RequestMethod.GET)
    public Boolean existEmailOnAnonymous(@RequestParam(required = true) String email,
                                    @RequestParam(required = true) String id,
                                    HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        UserProfile userProfile = userService.findByNEIdAndEmail(id.trim(), email.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "회원 프로필 업데이트 시 별명 중복 체크")
    @RequestMapping(value = "/exist/username/update", method = RequestMethod.GET)
    public Boolean existUsernameOnUpdate(@RequestParam(required = true) String username,
                                       HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        if (Objects.isNull(commonPrincipal.getId()))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        UserProfile userProfile = userService.findByNEIdAndUsername(commonPrincipal.getId().trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "// 비 로그인 상태(id를 제외한)에서 별명 중복 체크")
    @RequestMapping(value = "/exist/username/anonymous", method = RequestMethod.GET)
    public Boolean existUsernameOnAnonymous(@RequestParam(required = true) String username,
                                       @RequestParam(required = true) String id,
                                       HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        UserProfile userProfile = userService.findByNEIdAndUsername(id.trim(), username.trim());

        if (Objects.nonNull(userProfile))
            throw new DuplicateDataException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.replicated.data"));

        return false;
    }

    @ApiOperation(value = "이메일 중복 여부")
    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public Boolean existEmail(@RequestParam(required = true) String email,
                           HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(email))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));

        Boolean existEmail = userService.existEmail(locale, email.trim());

        return existEmail;
    }

    @ApiOperation(value = "별명 중복 여부")
    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public Boolean existUsername(@RequestParam(required = true) String username,
                              HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        Boolean existUsername = userService.existUsernameOnWrite(locale, username.trim());

        return existUsername;
    }

    @ApiOperation(value = "내 프로필")
    @RequestMapping(value = "/profile/me", method = RequestMethod.GET)
    public CommonPrincipal getMyProfile(HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        if (Objects.isNull(commonPrincipal))
            throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

        return commonPrincipal;
    }
}
