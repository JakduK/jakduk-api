package com.jakduk.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.common.CommonConst;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.service.BoardFreeService;
import com.jakduk.service.CommonService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardFreeService boardFreeService;
	
	@Autowired
	private CommonService commonService;
	
	@Resource
	LocaleResolver localeResolver;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/free", method = RequestMethod.GET)
	public String free(@ModelAttribute BoardListInfo boardListInfo, Model model
			, HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		boardFreeService.getFree(model, locale, boardListInfo);
		
		return "board/free";
	}
	
	@RequestMapping(value = "/free/{seq}", method = RequestMethod.GET)
	public String view(@PathVariable int seq, @ModelAttribute BoardListInfo boardListInfo, Model model
			, HttpServletRequest request, HttpServletResponse response) {
		
		Locale locale = localeResolver.resolveLocale(request);	
		Boolean isAddCookie = commonService.addViewsCookie(request, response, CommonConst.BOARD_NAME_FREE, seq);
		boardFreeService.getFreeView(model, locale, seq, boardListInfo, isAddCookie);
		
		return "board/freeView";
	}
	
	@RequestMapping(value = "/like/{seq}")
	public void setLike(@PathVariable int seq, Model model) {
		
		boardFreeService.setUsersFeelings(model, seq, CommonConst.BOARD_USERS_FEELINGS_TYPE_LIKE);
	}
	
	@RequestMapping(value = "/dislike/{seq}")
	public void setDislike(@PathVariable int seq, Model model) {
		
		boardFreeService.setUsersFeelings(model, seq, CommonConst.BOARD_USERS_FEELINGS_TYPE_DISLIKE);
	}
	
	@RequestMapping(value = "/free/comment/write", method = RequestMethod.POST)
	public void freeCommentWrite(Model model,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) Integer seq) {
		
		boardFreeService.freeCommentWrite(seq, content);
	}
	
	@RequestMapping(value = "/free/comment/{seq}", method = RequestMethod.GET)
	public void freeComment(@PathVariable int seq,
			Model model,			
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		
		boardFreeService.getFreeComment(model, seq, page, size);
		
//		return "board/free/comment";
	}
	
	@RequestMapping(value = "/free/comment/count/{seq}", method = RequestMethod.GET)
	public void freeCommentCount(@PathVariable int seq, Model model) {
		
		boardFreeService.getFreeCommentCount(model, seq);
		
	}
	
	@RequestMapping(value = "/comment/like/{seq}")
	public void setCommentLike(@PathVariable int seq, Model model,
			@RequestParam(required = true) String id) {
		
		boardFreeService.setUsersCommentFeelings(model, seq, id, CommonConst.BOARD_USERS_FEELINGS_TYPE_LIKE);
	}
	
	@RequestMapping(value = "/comment/dislike/{seq}")
	public void setCommentDislike(@PathVariable int seq, Model model,
			@RequestParam(required = true) String id) {
		
		boardFreeService.setUsersCommentFeelings(model, seq, id, CommonConst.BOARD_USERS_FEELINGS_TYPE_DISLIKE);
	}

}
