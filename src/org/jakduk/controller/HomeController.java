package org.jakduk.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	
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
	
	@RequestMapping(value = "/sample")
	public String sample(Model model) {
		
//		logger.debug("home");
		
		return "home/sample";
	}

}
