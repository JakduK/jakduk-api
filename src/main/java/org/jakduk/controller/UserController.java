package org.jakduk.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.jakduk.model.User;
import org.jakduk.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
//	private MemberService memberService;
	
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping(value = "/list")
	public void list(Model model) {
//		logger.debug("/list : " + model);
		
		List<User> users = userService.findAll();
		logger.debug("/list : " + users);
		
		model.addAttribute("list", users);
	}
	
	@RequestMapping(value = "/add")
	public void add() {
		logger.debug("/add : ");
		
		User user = new User();
		user.setId("112");
		user.setUserName("Gwangsu");
		userService.create(user);
	}

}
