package com.jakduk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:home";
	}
	
	@RequestMapping(value = "/home")
	public String home(Model model) {
		
//		logger.debug("home");
		
		return "home/home";
	}
	
	@RequestMapping(value = "/home/jumbotron")
	public String jumbotron(HttpServletRequest request, HttpServletResponse response,			
			@RequestParam(required = false) String lang,
			Model model) {
		
		String language = commonService.getLanguageCode(request, response, lang);
		
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
		
		Boolean existUsername = userService.existUsername(username);
		model.addAttribute("existUsername", existUsername);
	}

}
