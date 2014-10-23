package com.jakduk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.jakduk.model.web.UserProfileWrite;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 23.
 * @desc     :
 */

@Controller
@RequestMapping("/user/profile")
@SessionAttributes({"userProfileWrite", "footballClubs"})
public class UserProfileController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String profile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String lang,
			Model model) {
		
		String language = commonService.getLanguageCode(request, response, lang);
		
		userService.getUserProfile(model, language);
		
		return "user/profile";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String profileUpdate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String lang,
			Model model) {
		
		String language = commonService.getLanguageCode(request, response, lang);
		
		userService.getUserProfileUpdate(model, language);
		
		return "user/profileUpdate";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String profileUpdate(@Valid UserProfileWrite userProfileWrite, BindingResult result, SessionStatus sessionStatus) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "user/profileUpdate";
		}
		
		userService.userProfileUpdate(userProfileWrite);
		sessionStatus.setComplete();
		
		return "redirect:/user/profile";
	}

}
