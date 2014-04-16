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
	public String root() {
		
		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/free")
	public String free(Model model) {
		
		return "board/free";
		
//		model.addAllAttribute("list", null);
	}
	
	@RequestMapping(value = "/free/write")
	public String freeWrite(Model model) {
		
		return "board/freeWrite";
		
//		model.addAllAttribute("list", null);
	}

}
