package com.jakduk.controller;

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

	@RequestMapping(value = "/free/delete/{seq}", method = RequestMethod.GET)
	public String deleteFree(@PathVariable int seq, Model model,
			HttpServletResponse response,
			@RequestParam(required = true) String type) throws IOException {

		/*
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
		*/

		return "redirect:/board/free";
	}
	
	@RequestMapping(value = "/notice/set/{seq}", method = RequestMethod.GET)
	public String setFreeNotice(@PathVariable int seq, Model model,
			HttpServletResponse response) throws IOException {

		/*
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
		*/
		
		return null;	
	}
	
	@RequestMapping(value = "/notice/cancel/{seq}", method = RequestMethod.GET)
	public String releaseFreeNotice(@PathVariable int seq, Model model,
			HttpServletResponse response) throws IOException {

		/*
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
		*/
		
		return null;	
	}

}
