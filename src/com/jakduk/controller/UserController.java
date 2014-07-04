package com.jakduk.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jakduk.model.db.User;
import com.jakduk.model.web.UserWrite;
import com.jakduk.service.UserService;

@Controller
@RequestMapping("/user")
@SessionAttributes("userWrite")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/user/list";
	}
	
	@RequestMapping(value = "/list")
	public void list(Model model) {
		
		logger.debug("/test : " + userService.testFindId("test02"));
		
		List<User> users = userService.findAll();
		logger.debug("/list : " + users);
		
		model.addAttribute("list", users);
		
//		return "user/list";
	}
	
	@RequestMapping(value = "/write")
	public void write(Model model) {
		model.addAttribute("userWrite", new UserWrite());
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String writeSubmit(@Valid UserWrite userWrite, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "user/write";
		}
		
		userService.checkUserWrite(userWrite, result);
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "user/write";
		}
		
		userService.userWrite(userWrite);
		
		return "redirect:/user/list";
	}
	
}
