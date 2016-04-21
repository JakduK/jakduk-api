package com.jakduk.controller.session;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.web.UserProfileForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

	// jakduk 회원 정보 편집 처리.
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

	// social 회원 가입 페이지.
	@RequestMapping(value = "/social", method = RequestMethod.GET)
	public String writeSocial(@RequestParam(required = false) String lang,
							  NativeWebRequest request,
							  Model model) {

		Locale locale = localeResolver.resolveLocale((HttpServletRequest) request.getNativeRequest());
		String language = commonService.getLanguageCode(locale, lang);

		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

		log.debug("phjang=" + connection.getDisplayName());
		log.debug("phjang=" + connection.getKey());
		log.debug("phjang=" + connection.getProfileUrl());
		log.debug("phjang=" + connection.getApi());

		if (Objects.isNull(connection))
			throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

		org.springframework.social.connect.UserProfile userProfile = connection.fetchUserProfile();

		log.debug("phjang=" + userProfile.getFirstName());
		log.debug("phjang=" + userProfile.getLastName());
		log.debug("phjang=" + userProfile.getId());
		log.debug("phjang=" + userProfile.getName());
		log.debug("phjang=" + userProfile.getUsername());
		log.debug("phjang=" + userProfile.getEmail());


		List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

		UserProfileForm user = new UserProfileForm();
		user.setEmail(userProfile.getEmail());
		user.setUsername(connection.getDisplayName());

		model.addAttribute("userProfileForm", user);
		model.addAttribute("footballClubs", footballClubs);

		return "user/socialWrite";
	}

	// social 회원 가입 처리.
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

	// social 회원 정보 편집 처리.
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
