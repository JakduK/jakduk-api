package com.jakduk.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.jakduk.model.BoardFree;
import com.jakduk.service.BoardFreeService;

@Controller
@RequestMapping("/board")
@SessionAttributes("boardFree")
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
		
		boardFreeService.getFree(model);
		
		return "board/free";
	}
	
	@RequestMapping(value = "/free/write")
	public String freeWrite(Model model) {
		
		boardFreeService.getWrite(model);
		
		return "board/freeWrite";
	}
	
	@RequestMapping(value = "/free/write", method = RequestMethod.POST)
	public String freeWriteSubmit(@Valid BoardFree boardFree, BindingResult result, SessionStatus sessionStatus) {
		
		if (result.hasErrors()) {
			return "board/freeWrite";
		}
		
		logger.debug("test " + boardFree);
		boardFreeService.write(boardFree);
		sessionStatus.setComplete();
		
		return "redirect:/board/free";
	}

}
