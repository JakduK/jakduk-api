package com.jakduk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SampleController {
		
	@RequestMapping(value = "/summernote01")
	public String summernote01(Model model) {
		
		return "sample/summernote01";
	}
	
	@RequestMapping(value = "/summernote02")
	public String summernote02(Model model) {
		
		return "sample/summernote02";
	}
	
	@RequestMapping(value = "/summernote03")
	public String summernote03(Model model) {
		
		return "sample/summernote03";
	}
	
	@RequestMapping(value = "/sample01")
	public String sample01(Model model) {
		
		return "sample/sample01";
	}
	
	@RequestMapping(value = "/sample02")
	public String sample02(Model model) {
		
		return "sample/sample02";
	}
	
	@RequestMapping(value = "/sample03")
	public String sample03(Model model) {
		
		return "sample/sample03";
	}
	
	@RequestMapping(value = "/sample04")
	public String sample04(Model model) {
		
		return "sample/sample04";
	}	
	
	@RequestMapping(value = "/angular01")
	public String angular01(Model model) {
		
		return "sample/angular01";
	}
	
	@RequestMapping(value = "/angular02")
	public String angular02(Model model) {
		
		return "sample/angular02";
	}
	
	@RequestMapping(value = "/oauth01")
	public String oauth01(Model model) {
		
//		logger.debug("home");
		
		model.addAttribute("returnUrl", "http://localhost:8080/jakduk/oauth/daum/callback");
		
		return "sample/oauth01";
	}
	
	@RequestMapping(value = "/upload01")
	public String upload01(Model model) {
		
//		logger.debug("home");
		
		model.addAttribute("returnUrl", "http://localhost:8080/jakduk/oauth/daum/callback");
		
		return "sample/upload01";
	}
	
	@RequestMapping(value = "/gallery01")
	public String gallery01(Model model) {
		
		return "sample/gallery01";
	}	
	
	@RequestMapping(value = "/stats01")
	public String stats01(Model model) {
		
		return "sample/stats01";
	}	

}
