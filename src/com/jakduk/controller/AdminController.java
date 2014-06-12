package com.jakduk.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakduk.service.AdminService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping("/settings")
	public String root(Model model,
			@RequestParam(required = false) String message) {
		
		model.addAttribute("message", message);
		return "admin/admin";
	}
	
	@RequestMapping(value = "/init")
	public String loginFailure() {

		String message = adminService.initData();
		
		return "redirect:/admin?message=" + message;
	}

}
