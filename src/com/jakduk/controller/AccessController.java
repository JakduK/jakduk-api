package com.jakduk.controller;

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
		
		model.addAttribute("status", status);
		return "access/login";
	}

	/**
	 * 손안댄지 한참.
	 * @return
	 */
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
