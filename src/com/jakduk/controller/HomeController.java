package com.jakduk.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.service.CommonService;
import com.jakduk.service.HomeService;
import com.jakduk.service.UserService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	LocaleResolver localeResolver;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:home";
	}
	
	@RequestMapping(value = "/home")
	public String home(Model model,
			HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		homeService.getHome(model, locale);
		
		return "home/home";
	}
	
	@RequestMapping(value = "/home/jumbotron", method = RequestMethod.GET)
	public String jumbotron(HttpServletRequest request, HttpServletResponse response,			
			@RequestParam(required = false) String lang,
			Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		homeService.getJumbotron(model, language);
		
		return "home/jumbotron";
	}
	
	@RequestMapping(value = "/sample")
	public String sample(Model model) {
		
//		logger.debug("home");
		
		return "home/sample";
	}
	
	@RequestMapping(value = "/sample02")
	public String sample02(Model model) {
		
//		logger.debug("home");
		
		return "home/sample02";
	}
	
	@RequestMapping(value = "/unipaper")
	public String unipaper(Model model) {
		
		return "home/unipaper";
	}
	
	@RequestMapping(value = "/angular01")
	public String angular01(Model model) {
		
		return "home/angular01";
	}
	
	@RequestMapping(value = "/angular02")
	public String angular02(Model model) {
		
		return "home/angular02";
	}
	
	@RequestMapping(value = "/oauth01")
	public String oauth01(Model model) {
		
//		logger.debug("home");
		
		model.addAttribute("returnUrl", "http://localhost:8080/jakduk/oauth/daum/callback");
		
		return "home/oauth01";
	}
	
	@RequestMapping(value = "/check/user/email")
	public void checkEmail(Model model,
			@RequestParam(required = true) String email) {
		
		Boolean existEmail = userService.existEmail(email);
		
		model.addAttribute("existEmail", existEmail);
	}
	
	@RequestMapping(value = "/check/user/username")
	public void checkUsername(Model model,
			@RequestParam(required = true) String username) {
		
		Boolean existUsername = userService.existUsernameOnWrite(username);
		
		model.addAttribute("existUsername", existUsername);
	}
	
	@RequestMapping(value = "/check/user/update/username")
	public void checkUpdateUsername(Model model,
			@RequestParam(required = true) String username) {
		
		Boolean existUsername = userService.existUsernameOnUpdate(username);
		
		model.addAttribute("existUsername", existUsername);
	}
	
	@RequestMapping(value = "/check/oauth/update/username")
	public void checkOAuthUpdateUsername(Model model,
			@RequestParam(required = true) String username) {
		
		Boolean existUsername = userService.existOAuthUsernameOnUpdate(username);
		
		model.addAttribute("existUsername", existUsername);
	}
	
	@RequestMapping(value = "/home/board/latest", method = RequestMethod.GET)
	public String boardLatest(Model model) {
		
		homeService.getBoardLatest(model);
		
		return "home/board/latest";
	}
	
	@RequestMapping(value = "/home/user/latest", method = RequestMethod.GET)
	public String userLatest(Model model) {
		
		homeService.getUserLatest(model);
		
		return "home/user/latest";
	}
	
	@RequestMapping(value = "/home/data/latest", method = RequestMethod.GET)
	public String dataLatest(Model model) {
		
		homeService.getBoardLatest(model);
		homeService.getUserLatest(model);
		
		return "home/user/latest";
	}

}
