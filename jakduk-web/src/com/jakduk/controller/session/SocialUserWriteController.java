package com.jakduk.controller.session;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.web.SocialUserForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by pyohwan on 16. 4. 14.
 */

@Controller
@Slf4j
@RequestMapping("/user")
@SessionAttributes({"SocialUserForm", "footballClubs"})
public class SocialUserWriteController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @RequestMapping(value = "/social", method = RequestMethod.GET)
    public String write(@RequestParam(required = false) String lang,
                        Model model,
                        NativeWebRequest request) {

        Locale locale = localeResolver.resolveLocale((HttpServletRequest) request.getNativeRequest());
        String language = commonService.getLanguageCode(locale, lang);

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

        if (Objects.isNull(connection))
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

        UserProfile userProfile = connection.fetchUserProfile();

        List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

        SocialUserForm user = new SocialUserForm();
        user.setEmail(userProfile.getEmail());
        user.setUsername(userProfile.getName());

        model.addAttribute("socialUserForm", user);
        model.addAttribute("footballClubs", footballClubs);

        return "user/socialWrite";
    }

    @RequestMapping(value = "/social", method = RequestMethod.POST)
    public String write(@Valid SocialUserForm socialUserForm,
                        BindingResult result,
                        SessionStatus sessionStatus,
                        WebRequest request) {

        if (result.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("result=" + result);
            }
            return "user/socialWrite";
        }

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        ConnectionKey connectionKey = connection.getKey();

        CommonConst.ACCOUNT_TYPE providerId = CommonConst.ACCOUNT_TYPE.valueOf(connectionKey.getProviderId().toUpperCase());
        String providerUserId = connectionKey.getProviderUserId();

        User user = userService.writeSocialUser(socialUserForm, providerId, providerUserId);

        sessionStatus.setComplete();

        userService.signUpSocialUser(user, request);

        return "redirect:/home";
    }

    @RequestMapping(value = "/social/profile/update", method = RequestMethod.GET)
    public String profileUpdate(@RequestParam(required = false) String lang,
                                HttpServletRequest request,
                                Model model) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, lang);

        userService.getOAuthProfileUpdate(model, language);

        return "user/socialProfileUpdate";
    }

    @RequestMapping(value = "/social/profile/update", method = RequestMethod.POST)
    public String profileUpdate(@Valid SocialUserForm socialUserForm, BindingResult result, SessionStatus sessionStatus,
                                WebRequest request) {

        if (result.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("result=" + result);
            }
            return "user/socialProfileUpdate";
        }

        // 실제 DB에서 중복 검사
        userService.checkOAuthProfileUpdate(socialUserForm, result);

        // 위 중복 검사를 통과 못할 경우.
        if (result.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("result=" + result);
            }
            return "user/socialProfileUpdate";
        }

        User user = userService.editSocialProfile(socialUserForm);

        sessionStatus.setComplete();

        userService.signUpSocialUser(user, request);

        return "redirect:/user/social/profile?status=1";
    }

}
