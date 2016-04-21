package com.jakduk.controller;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.jakduk.authentication.jakduk.JakdukPrincipal;
import com.jakduk.authentication.social.SocialUserDetail;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.model.web.UserProfileForm;
import com.jakduk.model.web.UserProfileInfo;
import com.jakduk.service.FootballService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.model.db.User;
import com.jakduk.model.web.UserPasswordUpdate;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private FootballService footballService;
	
	@Resource
	LocaleResolver localeResolver;

	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@RequestMapping
	public String root() {
		
		return "redirect:/user/profile";
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public String refresh() {

		return "redirect:/user/profile";
	}

	@RequestMapping(value = "/social/refresh", method = RequestMethod.GET)
	public String socialRefresh() {

		return "redirect:/user/social/profile";
	}

	// 사용 안함.
	@RequestMapping(value = "/list")
	public void list(Model model) {

		log.debug("/test : " + userService.testFindId("test02"));
		
		List<User> users = userService.findAll();
		log.debug("/list : " + users);
		
		model.addAttribute("list", users);
	}

	// jakduk 회원 가입 페이지.
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@RequestParam(required = false) String lang,
						HttpServletRequest request,
						Model model) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		userService.getUserWrite(model, language);

		return "user/write";
	}

	// jakduk 회원 정보 페이지.
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(@RequestParam(required = false) String lang,
						  @RequestParam(required = false) Integer status,
						  HttpServletRequest request,
						  Model model) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukPrincipal) {
			JakdukPrincipal authUser = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			UserProfile user = userService.getUserProfileById(authUser.getId());

			UserProfileInfo userProfileInfo = new UserProfileInfo();
			userProfileInfo.setEmail(user.getEmail());
			userProfileInfo.setUsername(user.getUsername());
			userProfileInfo.setAbout(user.getAbout());

			FootballClub footballClub = user.getSupportFC();

			LocalName localName = footballService.getLocalNameOfFootballClub(footballClub, language);

			if (Objects.nonNull(localName)) userProfileInfo.setFootballClubName(localName);

			model.addAttribute("status", status);
			model.addAttribute("userProfile", userProfileInfo);

			return "user/profile";
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));
		}
	}

	// jakduk 회원 정보 편집 페이지.
	@RequestMapping(value = "/profile/update", method = RequestMethod.GET)
	public String updateProfile(HttpServletRequest request,
								@RequestParam(required = false) String lang,
								Model model) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukPrincipal) {
			JakdukPrincipal authUser = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserProfile user = userService.getUserProfileById(authUser.getId());

			UserProfileForm userProfileForm = new UserProfileForm();
			userProfileForm.setEmail(user.getEmail());
			userProfileForm.setUsername(user.getUsername());
			userProfileForm.setAbout(user.getAbout());

			FootballClub footballClub = user.getSupportFC();

			if (Objects.nonNull(footballClub)) {
				userProfileForm.setFootballClub(footballClub.getId());
			}

			List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

			model.addAttribute("userProfileForm", userProfileForm);
			model.addAttribute("footballClubs", footballClubs);

			return "user/profileUpdate";
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));
		}
	}

	@RequestMapping(value = "/password/update", method = RequestMethod.GET)
	public String passwordUpdate(Model model) {
		
		userService.getUserPasswordUpdate(model);
		
		return "user/passwordUpdate";
	}
	
	@RequestMapping(value = "/password/update", method = RequestMethod.POST)
	public String passwordUpdate(@Valid UserPasswordUpdate userPasswordUpdate, BindingResult result) {
		
		if (result.hasErrors()) {
			log.debug("result=" + result);
			return "user/passwordUpdate";
		}
		
		userService.checkUserPasswordUpdate(userPasswordUpdate, result);
		
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "user/passwordUpdate";
		}
		
		userService.userPasswordUpdate(userPasswordUpdate);
		
		return "redirect:/user/profile?status=2";
	}

	// social 회원 정보 페이지.
	@RequestMapping(value = "/social/profile", method = RequestMethod.GET)
	public String socialProfile(@RequestParam(required = false) String lang,
								@RequestParam(required = false) Integer status,
								HttpServletRequest request,
								Model model) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
			SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			UserProfile user = userService.getUserProfileById(userDetail.getId());

			UserProfileInfo profileInfo = new UserProfileInfo();
			profileInfo.setEmail(user.getEmail());
			profileInfo.setUsername(user.getUsername());
			profileInfo.setAbout(user.getAbout());

			FootballClub footballClub = user.getSupportFC();

			LocalName localName = footballService.getLocalNameOfFootballClub(footballClub, language);

			if (Objects.nonNull(localName)) profileInfo.setFootballClubName(localName);

			model.addAttribute("userProfile", profileInfo);
			model.addAttribute("status", status);
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));
		}

		return "user/socialProfile";
	}

	// social 회원 정보 편집 페이지.
	@RequestMapping(value = "/social/profile/update", method = RequestMethod.GET)
	public String updateSocialProfile(@RequestParam(required = false) String lang,
								HttpServletRequest request,
								Model model) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
			SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserProfile userProfile = userService.getUserProfileById(userDetail.getId());

			UserProfileForm userProfileForm = new UserProfileForm();
			userProfileForm.setEmail(userProfile.getEmail());
			userProfileForm.setUsername(userProfile.getUsername());
			userProfileForm.setAbout(userProfile.getAbout());

			List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);
			FootballClub footballClub = userProfile.getSupportFC();

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
}
