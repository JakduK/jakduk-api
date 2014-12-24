package com.jakduk.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
public class AccessController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,
			Model model,
			@RequestParam(required = false) Integer status) throws UnsupportedEncodingException {
		
		String refererUrl = request.getHeader("REFERER");
		
		model.addAttribute("loginRedirect", URLEncoder.encode(refererUrl, "UTF-8"));
		model.addAttribute("status", status);
		
		return "access/login";
	}

	@RequestMapping(value = "/logout/success")
	public String logoutSuccess(HttpServletRequest request) {
		
		String refererUrl = request.getHeader("REFERER");
		
		return "redirect:" + refererUrl;
	}
	
	@RequestMapping(value = "/denied")
	public String denied() {
		return "access/denied";
	}
}
