package com.jakduk.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jakduk.model.BoardFree;
import com.jakduk.service.BoardFreeService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardFreeService boardFreeService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/free")
	public String free(Model model) {
		
		return "board/free";
	}
	
	@RequestMapping(value = "/free/write")
	public String freeWrite(Model model) {
		
		model.addAttribute("board", new BoardFree());
		return "board/freeWrite";
	}
	
	@RequestMapping(value = "/free/write", method = RequestMethod.POST)
	public String freeWriteSubmit(@Valid BoardFree board, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("error=" + result.toString());
			return "board/free";
		}
		
		logger.debug("test " + board);
		boardFreeService.write(board);
		
		return "redirect:/board/free";
	}

}
