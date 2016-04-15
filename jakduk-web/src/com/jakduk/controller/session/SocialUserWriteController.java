package com.jakduk.controller.session;

import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.embedded.SocialInfo;
import com.jakduk.model.web.SocialUserForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by pyohwan on 16. 4. 14.
 */

@Controller
@Slf4j
@RequestMapping("/social")
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

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String write(@RequestParam(required = false) String lang,
                        Model model,
                        NativeWebRequest request) {

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        UserProfile userProfile = connection.fetchUserProfile();

        ConnectionKey connectionKey = connection.getKey();

        log.debug("connectionKey=" + connectionKey.getProviderId());
        log.debug("connectionKey=" + connectionKey.getProviderUserId());
        log.debug("connectionKey=" + connection.getDisplayName());

        log.debug("userProfile=" + userProfile.getId());
        log.debug("userProfile=" + userProfile.getEmail());
        log.debug("userProfile=" + userProfile.getUsername());
        log.debug("userProfile=" + userProfile.getName());
        log.debug("userProfile=" + userProfile.getFirstName());
        log.debug("userProfile=" + userProfile.getLastName());

        Locale locale = localeResolver.resolveLocale((HttpServletRequest) request.getNativeRequest());
        String language = commonService.getLanguageCode(locale, lang);
        List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

        SocialUserForm user = new SocialUserForm();
        user.setEmail(userProfile.getEmail());
        user.setUsername(userProfile.getName());

        model.addAttribute("socialUserForm", user);
        model.addAttribute("footballClubs", footballClubs);

        return "user/socialUserWrite";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String write(@Valid SocialUserForm socialUserForm,
                        BindingResult result,
                        SessionStatus sessionStatus,
                        WebRequest request) {

        if (result.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("result=" + result);
            }
            return "user/socialUserWrite";
        }

        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        ConnectionKey connectionKey = connection.getKey();

        log.debug("socialUserForm=" + socialUserForm);

        SocialInfo socialInfo = new SocialInfo();
        socialInfo.setProviderId(CommonConst.ACCOUNT_TYPE.valueOf(connectionKey.getProviderId().toUpperCase()));
        socialInfo.setOauthId(connectionKey.getProviderUserId());

        User user = userService.saveSocialUser(socialUserForm, socialInfo);

        OAuthPrincipal userDetails = new OAuthPrincipal(user.getId(), socialInfo.getOauthId(), user.getUsername(), socialInfo.getProviderId(),
                true, true, true, true, getAuthorities(user.getRoles()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //sessionStatus.setComplete();

        providerSignInUtils.doPostSignUp(socialUserForm.getEmail(), request);

        return "redirect:/home";
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(roles));

        return authList;
    }

    public List<String> getRoles(List<Integer> roles) {
        List<String> newRoles = new ArrayList<String>();

        if (roles != null) {
            for (Integer roleNumber : roles) {
                String roleName = CommonRole.getRoleName(roleNumber);
                if (!roleName.isEmpty()) {
                    newRoles.add(roleName);
                }
            }
        }

        return newRoles;
    }

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
