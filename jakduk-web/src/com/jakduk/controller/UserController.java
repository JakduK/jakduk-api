package com.jakduk.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@Resource
	LocaleResolver localeResolver;

	@RequestMapping
	public String root() {
		
		return "redirect:/user/profile";
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public String freeRefresh() {

		return "redirect:/user/profile";
	}

	@RequestMapping(value = "/list")
	public void list(Model model) {

		log.debug("/test : " + userService.testFindId("test02"));
		
		List<User> users = userService.findAll();
		log.debug("/list : " + users);
		
		model.addAttribute("list", users);
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String lang,
			@RequestParam(required = false) Integer status,
			Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		userService.getUserProfile(model, language, status);
		
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
}
