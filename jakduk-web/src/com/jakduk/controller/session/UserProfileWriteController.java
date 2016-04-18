package com.jakduk.controller.session;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import com.jakduk.authentication.social.SocialUserDetail;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.model.web.UserProfileForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
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

import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     :
 */

@Controller
@Slf4j
@RequestMapping("/user")
@SessionAttributes({"userProfileForm", "footballClubs"})
public class UserProfileWriteController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	LocaleResolver localeResolver;

	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@RequestMapping(value = "/profile/update", method = RequestMethod.GET)
	public String profileUpdate(HttpServletRequest request,
			@RequestParam(required = false) String lang,
			Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		userService.getUserProfileUpdate(model, language);
		
		return "user/profileUpdate";
	}
	
	@RequestMapping(value = "/profile/update", method = RequestMethod.POST)
	public String profileUpdate(@Valid UserProfileForm userProfileForm, BindingResult result, SessionStatus sessionStatus) {
		
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "user/profileUpdate";
		}
		
		userService.checkProfileUpdate(userProfileForm, result);
		
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "user/profileUpdate";
		}
		
		userService.userProfileUpdate(userProfileForm);
		sessionStatus.setComplete();
		
		return "redirect:/user/profile?status=1";
	}

	@RequestMapping(value = "/social", method = RequestMethod.GET)
	public String write(@RequestParam(required = false) String lang,
						Model model,
						NativeWebRequest request) {

		Locale locale = localeResolver.resolveLocale((HttpServletRequest) request.getNativeRequest());
		String language = commonService.getLanguageCode(locale, lang);

		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

		if (Objects.isNull(connection))
			throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

		org.springframework.social.connect.UserProfile userProfile = connection.fetchUserProfile();

		List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

		UserProfileForm user = new UserProfileForm();
		user.setEmail(userProfile.getEmail());
		user.setUsername(userProfile.getName());

		model.addAttribute("userProfileForm", user);
		model.addAttribute("footballClubs", footballClubs);

		return "user/socialWrite";
	}

	@RequestMapping(value = "/social", method = RequestMethod.POST)
	public String write(@Valid UserProfileForm userProfileForm,
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

		User user = userService.writeSocialUser(userProfileForm, providerId, providerUserId);

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

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
			SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserProfile userProfile = userService.getUserProfileById(userDetail.getId());

			List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

			FootballClub footballClub = userProfile.getSupportFC();

			UserProfileForm userProfileForm = new UserProfileForm();
			userProfileForm.setEmail(userProfile.getEmail());
			userProfileForm.setUsername(userProfile.getUsername());
			userProfileForm.setAbout(userProfile.getAbout());

			if (Objects.nonNull(footballClub)) {
				userProfileForm.setFootballClub(footballClub.getId());
			}

			model.addAttribute("userProfileForm", userProfileForm);
			model.addAttribute("footballClubs", footballClubs);

			return "user/socialProfileUpdate";
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));
		}
	}

	@RequestMapping(value = "/social/profile/update", method = RequestMethod.POST)
	public String profileUpdate(@Valid UserProfileForm userProfileForm,
								BindingResult result,
								SessionStatus sessionStatus,
								WebRequest request) {

		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "user/socialProfileUpdate";
		}

		// 실제 DB에서 중복 검사
		userService.checkSocialProfileUpdate(userProfileForm, result);

		// 위 중복 검사를 통과 못한 경우, 메시지 출력
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "user/socialProfileUpdate";
		}

		User user = userService.editSocialProfile(userProfileForm);

		sessionStatus.setComplete();

		userService.signUpSocialUser(user, request);

		return "redirect:/user/social/profile?status=1";
	}

}
