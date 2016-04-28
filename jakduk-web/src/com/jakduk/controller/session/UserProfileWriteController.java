package com.jakduk.controller.session;

import com.jakduk.authentication.jakduk.JakdukPrincipal;
import com.jakduk.authentication.social.SocialUserDetail;
import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.model.web.user.UserProfileForm;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import com.jakduk.service.UserService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
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

	@Resource
	LocaleResolver localeResolver;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private FootballService footballService;

	@Autowired
	private ProviderSignInUtils providerSignInUtils;

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

			List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

			model.addAttribute("userProfileForm", userProfileForm);
			model.addAttribute("footballClubs", footballClubs);

			return "user/profileUpdate";
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));
		}
	}

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

		if (Objects.isNull(connection))
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

		ConnectionKey connectionKey = connection.getKey();

		if (Objects.isNull(connection))
			throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

		org.springframework.social.connect.UserProfile socialProfile = connection.fetchUserProfile();

		List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

		CommonConst.ACCOUNT_TYPE providerId = CommonConst.ACCOUNT_TYPE.valueOf(connectionKey.getProviderId().toUpperCase());
		String providerUserId = connectionKey.getProviderUserId();

		UserProfileForm user = new UserProfileForm();
		user.setEmail(socialProfile.getEmail());
		user.setUsername(connection.getDisplayName());

		// Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
		UserProfile userProfile = userService.findOneByProviderIdAndProviderUserId(providerId, providerUserId);

		if (Objects.nonNull(userProfile))
			user.setId(userProfile.getId());

		model.addAttribute("userProfileForm", user);
		model.addAttribute("footballClubs", footballClubs);

		return "user/socialWrite";
	}

	// social 회원 가입 처리.
	@RequestMapping(value = "/social", method = RequestMethod.POST)
	public String write(@Valid UserProfileForm userProfileForm,
						BindingResult result,
						SessionStatus sessionStatus,
						NativeWebRequest request) {

		Locale locale = localeResolver.resolveLocale((HttpServletRequest) request.getNativeRequest());

		// 첫 번째 검증.
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "user/socialWrite";
		}

		// Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
		if (Objects.nonNull(userProfileForm.getId()) && userProfileForm.getId().isEmpty() == true) {
			this.checkValidationUserProfileOnAnonymous(userProfileForm, result);
		} else {
			this.checkValidationUserProfileOnWrite(userProfileForm, result);
		}

		// 위 검사를 통과 못한 경우, 메시지 출력
		if (result.hasErrors()) {
			log.debug("result=" + result);
			return "user/socialWrite";
		}

		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		ConnectionKey connectionKey = connection.getKey();

		CommonConst.ACCOUNT_TYPE providerId = CommonConst.ACCOUNT_TYPE.valueOf(connectionKey.getProviderId().toUpperCase());
		String providerUserId = connectionKey.getProviderUserId();

		User user = new User();

		// Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
		if (Objects.nonNull(userProfileForm.getId()) && userProfileForm.getId().isEmpty() == true) {
			user = userService.findById(userProfileForm.getId());
		}

		user.setEmail(userProfileForm.getEmail().trim());
		user.setUsername(userProfileForm.getUsername().trim());
		user.setProviderId(providerId);
		user.setProviderUserId(providerUserId);

		ArrayList<Integer> roles = new ArrayList<Integer>();
		roles.add(CommonRole.ROLE_NUMBER_USER_01);

		user.setRoles(roles);

		String footballClub = userProfileForm.getFootballClub();
		String about = userProfileForm.getAbout();

		if (Objects.nonNull(footballClub) && !footballClub.isEmpty()) {
			FootballClub supportFC = footballService.findById(footballClub);

			user.setSupportFC(supportFC);
		}

		if (Objects.nonNull(about) && !about.isEmpty()) {
			user.setAbout(userProfileForm.getAbout().trim());
		}

		User returnUser = userService.addSocialUser(user);

		sessionStatus.setComplete();

		userService.signUpSocialUser(returnUser, request);

		return "redirect:/home";
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

			List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);
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
		this.checkValidationUserProfileOnUpdate(userProfileForm, result);

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

	// SNS 계정의 회원 정보를 공통으로 검증.
	private void checkCommonValidationUserProfile(UserProfileForm userProfileForm, BindingResult result) {
		CommonConst.VALIDATION_TYPE emailStatus = userProfileForm.getEmailStatus();
		CommonConst.VALIDATION_TYPE usernameStatus = userProfileForm.getUsernameStatus();

		if (emailStatus.equals(CommonConst.VALIDATION_TYPE.OK) == false) {
			result.rejectValue("email", "user.msg.need.validation.email");
		}

		if (usernameStatus.equals(CommonConst.VALIDATION_TYPE.OK) == false) {
			result.rejectValue("username", "user.msg.need.validation.username");
		}
	}

	// SNS 계정의 회원 가입 시 DB에 쿼리하여 중복 체크한다.
	private void checkValidationUserProfileOnWrite(UserProfileForm userProfileForm, BindingResult result) {

		String email = userProfileForm.getEmail();
		String username = userProfileForm.getUsername();

		this.checkCommonValidationUserProfile(userProfileForm, result);

		UserProfile existEmail = userService.findOneByEmail(email);
		if (Objects.nonNull(existEmail))
			result.rejectValue("email", "user.msg.already.email");

		UserProfile existUsername = userService.findOneByUsername(username);
		if (Objects.nonNull(existUsername))
			result.rejectValue("username", "user.msg.already.username");
	}

	// SNS 계정의 회원 정보 편집 시 DB에 쿼리하여 중복 체크한다.
	private void checkValidationUserProfileOnUpdate(UserProfileForm userProfileForm, BindingResult result) {

		SocialUserDetail principal = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String id = principal.getId();
		String email = userProfileForm.getEmail();
		String username = userProfileForm.getUsername();

		this.checkCommonValidationUserProfile(userProfileForm, result);

		UserProfile existEmail = userService.findByNEIdAndEmail(id, email);
		if (Objects.nonNull(existEmail))
			result.rejectValue("email", "user.msg.already.email");

		UserProfile existUsername = userService.findByNEIdAndUsername(id, username);
		if (Objects.nonNull(existUsername))
			result.rejectValue("username", "user.msg.already.username");
	}

	// SNS 계정의 회원 가입 시 DB에 쿼리하여 중복 체크한다.
	// Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
	private void checkValidationUserProfileOnAnonymous(UserProfileForm userProfileForm, BindingResult result) {
		String id = userProfileForm.getId();
		String email = userProfileForm.getEmail();
		String username = userProfileForm.getUsername();

		this.checkCommonValidationUserProfile(userProfileForm, result);

		UserProfile existEmail = userService.findByNEIdAndEmail(id, email);
		if (Objects.nonNull(existEmail))
			result.rejectValue("email", "user.msg.already.email");

		UserProfile existUsername = userService.findByNEIdAndUsername(id, username);
		if (Objects.nonNull(existUsername))
			result.rejectValue("username", "user.msg.already.username");
	}

}
