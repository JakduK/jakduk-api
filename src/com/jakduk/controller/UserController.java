package com.jakduk.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.model.db.User;
import com.jakduk.model.web.UserPasswordUpdate;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	LocaleResolver localeResolver;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/user/list";
	}
	
	@RequestMapping(value = "/list")
	public void list(Model model) {
		
		logger.debug("/test : " + userService.testFindId("test02"));
		
		List<User> users = userService.findAll();
		logger.debug("/list : " + users);
		
		model.addAttribute("list", users);
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String lang,
			Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		userService.getUserProfile(model, language);
		
		return "user/profile";
	}
	
	@RequestMapping(value = "/password/update", method = RequestMethod.GET)
	public String passwordUpdate(Model model) {
		
		userService.getUserPasswordUpdate(model);
		
		return "user/passwordUpdate";
	}
	
	@RequestMapping(value = "/password/update", method = RequestMethod.POST)
	public String passwordUpdate(@Valid UserPasswordUpdate userPasswordUpdate, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "user/passwordUpdate";
		}
		
		userService.checkUserPasswordUpdate(userPasswordUpdate, result);
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "user/passwordUpdate";
		}
		
		userService.userPasswordUpdate(userPasswordUpdate);
		
		return "redirect:/user/profile";
	}
}
