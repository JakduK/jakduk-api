package com.jakduk.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakduk.service.HomeService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private HomeService homeService;
	
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
	public void jumbotron(Model model,
			@RequestParam(required = false) String lang) {
		
		homeService.getJumbotron(model, lang);
	}
	
	@RequestMapping(value = "/sample")
	public String sample(Model model) {
		
//		logger.debug("home");
		
		return "home/sample";
	}

}
