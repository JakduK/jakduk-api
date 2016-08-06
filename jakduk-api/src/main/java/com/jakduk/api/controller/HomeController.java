package com.jakduk.api.controller;

import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.HomeService;
import com.jakduk.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

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
	
	@Resource
	MessageSource messageSource;
	
	@RequestMapping
	public String root() {
		
		return "redirect:/home";
	}

	@RequestMapping(value = "/error/{code}", method = RequestMethod.GET)
	public String error(Model model, @PathVariable String code) {
		
		model.addAttribute("code", code);
		
		return "access/error";
	}
	
	@RequestMapping(value = "/rss")
	public void rss(Model model
			, HttpServletRequest request, HttpServletResponse response) {
		
		Locale locale = localeResolver.resolveLocale(request);
		Integer status = homeService.getRss(response, locale, messageSource);
	}

}
