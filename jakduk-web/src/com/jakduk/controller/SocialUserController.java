package com.jakduk.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.authentication.social.SocialUserDetail;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.model.simple.OAuthProfile;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.model.web.OAuthProfileInfo;
import com.jakduk.model.web.UserProfileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 17.
 * @desc     :
 */

@Controller
@RequestMapping("/social/user")
public class SocialUserController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	LocaleResolver localeResolver;

	@RequestMapping
	public String root() {

		return "redirect:/social/user/profile";
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public String freeRefresh() {

		return "redirect:/social/user/profile";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(@RequestParam(required = false) String lang,
						  @RequestParam(required = false) Integer status,
						  HttpServletRequest request,
						  Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
			SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			UserProfile user = userService.userProfileFindById(userDetail.getId());

			UserProfileInfo profileInfo = new UserProfileInfo();
			profileInfo.setEmail(user.getEmail());
			profileInfo.setUsername(user.getUsername());
			profileInfo.setAbout(user.getAbout());

			FootballClub footballClub = user.getSupportFC();

			if (Objects.nonNull(footballClub)) {
				List<LocalName> names = footballClub.getNames();

				for (LocalName name : names) {
					if (name.getLanguage().equals(language)) {
						profileInfo.setFootballClubName(name);
					}
				}
			}

			model.addAttribute("userProfile", profileInfo);
			model.addAttribute("status", status);
		} else {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));
		}
		
		return "user/socialProfile";
	}
	
}
