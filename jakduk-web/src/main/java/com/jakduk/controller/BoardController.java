package com.jakduk.controller;

import com.jakduk.common.CommonConst;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.service.BoardFreeService;
import com.jakduk.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardFreeService boardFreeService;
	
	@Autowired
	private CommonService commonService;
	
	@Resource
	LocaleResolver localeResolver;

	@RequestMapping(value = "/free/comments/refresh", method = RequestMethod.GET)
	public String freeCommentsRefresh() {
		
		return "redirect:/board/free/comments";
	}	

	
	@RequestMapping(value = "/free/comments", method = RequestMethod.GET)
	public String freeCommentsList(@ModelAttribute BoardListInfo boardListInfo, Model model
			, HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		boardFreeService.getFreeCommentsList(model, locale, boardListInfo);
		
		return "board/freeComments";
	}
	
	@RequestMapping(value = "/free/{seq}", method = RequestMethod.GET)
	public String freeView(@PathVariable int seq, @ModelAttribute BoardListInfo listInfo, Model model
			, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String result) throws IOException {
		
		Locale locale = localeResolver.resolveLocale(request);	
		Boolean isAddCookie = commonService.addViewsCookie(request, response, CommonConst.COOKIE_NAME_BOARD_FREE, String.valueOf(seq));
		Integer status = boardFreeService.getFreeView(model, locale, seq, listInfo, isAddCookie);
		
		if (!status.equals(HttpServletResponse.SC_OK)) {
			response.sendError(status);
			return null;
		}
		
		if (result != null && !result.isEmpty()) {
			model.addAttribute("result", result);
		}
		
		return "board/freeView";
	}

	@RequestMapping(value = "/free/delete/{seq}", method = RequestMethod.GET)
	public String deleteFree(@PathVariable int seq, Model model,
			HttpServletResponse response,
			@RequestParam(required = true) String type) throws IOException {
		
		Integer status = boardFreeService.deleteFree(model, seq, type);
		
		if (status.equals(HttpServletResponse.SC_UNAUTHORIZED)) {
			response.sendError(status);
			return null;	
		} else if (status.equals(HttpServletResponse.SC_NOT_ACCEPTABLE)) {
			if (type.equals(CommonConst.BOARD_DELETE_TYPE_ALL)) {
				return "redirect:/board/free/" + seq + "?result=existComment";
			} else if (type.equals(CommonConst.BOARD_DELETE_TYPE_POSTONLY)) {
				return "redirect:/board/free/" + seq + "?result=emptyComment";
			}
		}

		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/notice/set/{seq}", method = RequestMethod.GET)
	public String setFreeNotice(@PathVariable int seq, Model model,
			HttpServletResponse response) throws IOException {
		
		Integer status = boardFreeService.setNotice(seq, CommonConst.COMMON_TYPE_SET);
		
		if (status.equals(HttpServletResponse.SC_OK)) {
			return "redirect:/board/free/" + seq + "?result=setNotice";
		} else if (status.equals(HttpServletResponse.SC_NOT_ACCEPTABLE)) {
			return "redirect:/board/free/" + seq + "?result=alreadyNotice";
		} else if (status.equals(HttpServletResponse.SC_UNAUTHORIZED)) {
			response.sendError(status);
		} else {
			response.sendError(status);
		}
		
		return null;	
	}
	
	@RequestMapping(value = "/notice/cancel/{seq}", method = RequestMethod.GET)
	public String releaseFreeNotice(@PathVariable int seq, Model model,
			HttpServletResponse response) throws IOException {
		
		Integer status = boardFreeService.setNotice(seq, CommonConst.COMMON_TYPE_CANCEL);
		
		if (status.equals(HttpServletResponse.SC_OK)) {
			return "redirect:/board/free/" + seq + "?result=cancelNotice";
		} else if (status.equals(HttpServletResponse.SC_NOT_ACCEPTABLE)) {
			return "redirect:/board/free/" + seq + "?result=alreadyNotNotice";
		} else if (status.equals(HttpServletResponse.SC_UNAUTHORIZED)) {
			response.sendError(status);
		} else {
			response.sendError(status);
		}
		
		return null;	
	}

}
