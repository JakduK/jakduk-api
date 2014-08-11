package com.jakduk.controller;

import java.net.MalformedURLException;
import java.net.URL;

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
			@RequestParam(required = false) Integer status) {
		
		URL url;
		String domainPath;
		try {
			url = new URL(request.getRequestURL().toString());
			domainPath = String.format("http://%s", url.getAuthority());
			
			model.addAttribute("domainPath", domainPath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("status", status);
		return "access/login";
	}

	@RequestMapping(value = "/logout/success")
	public String logoutSuccess() {
		String message = "Logout Success!";
		return "redirect:/login?message=" + message;
	}
	
	@RequestMapping(value = "/denied")
	public String denied() {
		return "access/denied";
	}
}
