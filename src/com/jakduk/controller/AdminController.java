package com.jakduk.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.web.UserWrite;
import com.jakduk.service.AdminService;
import com.jakduk.service.HomeService;

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
	
	@RequestMapping
	public String root() {
		
		return "redirect:/admin/settings";
	}
	
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
	
	@RequestMapping(value = "/encyclopedia/write")
	public String shortHistoryWrite(Model model) {
		model.addAttribute("encyclopedia", new Encyclopedia());
		
		return "admin/encyclopediaWrite";
	}
	
	@RequestMapping(value = "/encyclopedia/write", method = RequestMethod.POST)
	public String shortHistoryWriteSubmit(@Valid Encyclopedia encyclopedia, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "encyclopedia/write";
		}
		
		adminService.shortHistoryWrite(encyclopedia);
		
		return "redirect:/encyclopedia/list";
	}

}
