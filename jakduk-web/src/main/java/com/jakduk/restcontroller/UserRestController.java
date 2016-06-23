package com.jakduk.restcontroller;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.exception.DuplicateDataException;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private UserService userService;

    @Autowired
    private CommonService commonService;

    // 회원 프로필 업데이트 시 Email 중복 체크.
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

    // 비로그인 상태(id를 제외한)에서 Email 중복 체크.
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

    // 회원 프로필 업데이트 시 별명 중복 체크.
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

    // 비 로그인 상태(id를 제외한)에서 별명 중복 체크.
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
