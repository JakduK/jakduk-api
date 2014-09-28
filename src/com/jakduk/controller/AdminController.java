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
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.web.FootballClubWrite;
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
		
		adminService.encyclopediaWrite(encyclopedia);
		
		return "redirect:/encyclopedia/list";
	}
	
	@RequestMapping(value = "/footballclub/write")
	public String footballClubWrite(Model model) {
		
		adminService.getFootballClubWrite(model);
		
		return "admin/footballClubWrite";
	}
	
	@RequestMapping(value = "/footballclub/write", method = RequestMethod.POST)
	public String footballClubWrite(@Valid FootballClubWrite footballClubWrite, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/footballClubWrite";
		}
		
		adminService.footballClubWrite(footballClubWrite);

		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/footballclub/origin/write")
	public String footballClubOriginWrite(Model model) {
		model.addAttribute("footballClubOriginWrite", new FootballClubOrigin());
		
		return "admin/footballClubOriginWrite";
	}
	
	@RequestMapping(value = "/footballclub/origin/write", method = RequestMethod.POST)
	public String footballClubOriginWrite(@Valid FootballClubOrigin footballClubOrigin, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/footballClubOriginWrite";
		}
		
		adminService.footballClubOriginWrite(footballClubOrigin);

		return "redirect:/admin";
	}

}
