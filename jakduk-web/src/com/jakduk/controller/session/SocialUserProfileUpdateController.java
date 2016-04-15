package com.jakduk.controller.session;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import com.jakduk.model.db.User;
import com.jakduk.model.web.SocialUserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 2.
 * @desc     :
 */

@Controller
@Slf4j
@RequestMapping("/social")
@SessionAttributes({"SocialUserForm", "footballClubs"})
public class SocialUserProfileUpdateController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	LocaleResolver localeResolver;

	@RequestMapping(value = "/user/profile/update", method = RequestMethod.GET)
	public String profileUpdate(@RequestParam(required = false) String lang,
								HttpServletRequest request,
								Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		userService.getOAuthProfileUpdate(model, language);
		
		return "user/socialProfileUpdate";
	}
	
	@RequestMapping(value = "/user/profile/update", method = RequestMethod.POST)
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
		
		return "redirect:/social/user/profile?status=1";
	}

}
