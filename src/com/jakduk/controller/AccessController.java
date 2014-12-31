package com.jakduk.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakduk.service.CommonService;

@Controller
@RequestMapping()
public class AccessController {
	
	@Autowired
	CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request,
			Model model,
			@RequestParam(required = false) Integer status,
			@RequestParam(required = false) String loginRedirect) throws UnsupportedEncodingException {
		
		if (loginRedirect == null) {
			loginRedirect = request.getHeader("REFERER");
		}
		
		if (loginRedirect != null) {
			model.addAttribute("loginRedirect", URLEncoder.encode(loginRedirect, "UTF-8"));
		}
		
		model.addAttribute("status", status);
		
		return "access/login";
	}

	@RequestMapping(value = "/logout/success")
	public String logoutSuccess(HttpServletRequest request) {
		
		String redirctUrl = "/";
		String refererUrl = request.getHeader("REFERER");
		
		if (refererUrl != null) {
			if (commonService.isRedirectUrl(refererUrl)) {
				redirctUrl = refererUrl;
			}
		}
		
		return "redirect:" + redirctUrl;
	}
	
	@RequestMapping(value = "/denied")
	public String denied() {
		return "access/denied";
	}
}
