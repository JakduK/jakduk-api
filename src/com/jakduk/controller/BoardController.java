package com.jakduk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.service.BoardFreeService;
import com.jakduk.service.CommonService;

@Controller
@RequestMapping("/board")
@SessionAttributes({"boardFree","boardCategorys"})
public class BoardController {
	
	@Autowired
	private BoardFreeService boardFreeService;
	
	@Autowired
	private CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/free", method = RequestMethod.GET)
	public String free(@ModelAttribute BoardListInfo boardListInfo, Model model) {
		
		logger.debug("page=" + boardListInfo);
		boardFreeService.getFree(model, boardListInfo);
		
		return "board/free";
	}
	
	@RequestMapping(value = "/free/{seq}", method = RequestMethod.GET)
	public String view(@PathVariable int seq, @ModelAttribute BoardListInfo boardListInfo, Model model
			, HttpServletRequest request, HttpServletResponse response) {
		
		Boolean isAddCookie = commonService.addViewsCookie(request, response, CommonConst.BOARD_NAME_FREE, seq);
		boardFreeService.getFreeView(model, seq, boardListInfo, isAddCookie);
		
		return "board/freeView";
	}
	
	@RequestMapping(value = "/free/write")
	public String freeWrite(Model model) {
		
		boardFreeService.getWrite(model);
		
		return "board/freeWrite";
	}
	
	@RequestMapping(value = "/free/write", method = RequestMethod.POST)
	public String freeWrite(@Valid BoardFree boardFree, BindingResult result, SessionStatus sessionStatus) {
		
		if (result.hasErrors()) {
			return "board/freeWrite";
		}
		
		boardFreeService.write(boardFree);
		sessionStatus.setComplete();
		
		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/good/{seq}")
	public void setGood(@PathVariable int seq, Model model) {
		
		boardFreeService.getGoodOrBad(model, seq, 1);
	}
	
	@RequestMapping(value = "/bad/{seq}")
	public void setBad(@PathVariable int seq, Model model) {
		
		boardFreeService.getGoodOrBad(model, seq, 2);
	}

}
