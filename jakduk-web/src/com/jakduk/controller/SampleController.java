package com.jakduk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
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
	
	@RequestMapping(value = "/gallery02")
	public String gallery02(Model model) {
		
		return "sample/gallery02";
	}		
	
	@RequestMapping(value = "/gallery03")
	public String gallery03(Model model) {
		
		return "sample/gallery03";
	}		
	
	@RequestMapping(value = "/stats01")
	public String stats01(Model model) {
		
		return "sample/stats01";
	}
	
	@RequestMapping(value = "/stats02")
	public String stats02(Model model) {
		
		return "sample/stats02";
	}
	
	@RequestMapping(value = "/home01")
	public String home01(Model model) {
		
		return "sample/home01";
	}	
	
	@RequestMapping(value = "/home02")
	public String home02(Model model) {
		
		return "sample/home02";
	}
	
	@RequestMapping(value = "/kakao01")
	public String kakaotalk01(Model model) {
		
		return "sample/kakao01";
	}
	
	@RequestMapping(value = "/scroll01")
	public String scroll01(Model model) {
		
		return "sample/scroll01";
	}
	
	@RequestMapping(value ="/search01")
	public String search01(Model model) {
		return "sample/search01";
	}
	
	@RequestMapping(value ="/pager01")
	public String pager01(Model model) {
		return "sample/pager01";
	}
	
	@RequestMapping(value ="/lightbox01")
	public String lightbox01(Model model) {
		return "sample/lightbox01";
	}
	
	@RequestMapping(value ="/slick01")
	public String slick01(Model model) {
		return "sample/slick01";
	}
	
	@RequestMapping(value ="/slick02")
	public String slick02(Model model) {
		return "sample/slick02";
	}
}
