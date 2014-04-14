package org.jakduk.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public void free(Model model) {
		
		//model.addAllAttribute("list", null);
	}

}
