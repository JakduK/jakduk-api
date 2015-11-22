package com.jakduk.controller;

import java.io.IOException;
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
		
		return "redirect:/board/free/posts";
	}

	@RequestMapping(value = "/free/refresh", method = RequestMethod.GET)
	public String freeRefresh() {
		
		return "redirect:/board/free/posts";
	}	
	
	@RequestMapping(value = "/free/posts/refresh", method = RequestMethod.GET)
	public String freePostsRefresh() {
		
		return "redirect:/board/free/posts";
	}	
	
	@RequestMapping(value = "/free/comments/refresh", method = RequestMethod.GET)
	public String freeCommentsRefresh() {
		
		return "redirect:/board/free/comments";
	}	
	
	@RequestMapping(value = "/free", method = RequestMethod.GET)
	public String freeList(@ModelAttribute BoardListInfo boardListInfo, Model model
			, HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		boardFreeService.getFree(model, locale, boardListInfo);
		
		return "board/freePosts";
	}
	
	@RequestMapping(value = "/free/posts", method = RequestMethod.GET)
	public String freePostsList(@ModelAttribute BoardListInfo boardListInfo, Model model
			, HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		boardFreeService.getFree(model, locale, boardListInfo);
		
		return "board/freePosts";
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
	
	@RequestMapping(value = "/like/{seq}")
	public void setFreeLike(@PathVariable int seq, Model model) {
		
		boardFreeService.setUsersFeelings(model, seq, CommonConst.FEELING_TYPE_LIKE);
	}
	
	@RequestMapping(value = "/dislike/{seq}")
	public void setFreeDislike(@PathVariable int seq, Model model) {
		
		boardFreeService.setUsersFeelings(model, seq, CommonConst.FEELING_TYPE_DISLIKE);
	}
	
	@RequestMapping(value = "/free/comment/write", method = RequestMethod.POST)
	public void freeCommentWrite(Model model,
			HttpServletRequest request,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) Integer seq) {
		
		boardFreeService.freeCommentWrite(request, seq, content);
	}
	
	@RequestMapping(value = "/free/comment/{seq}", method = RequestMethod.GET)
	public void freeComment(@PathVariable int seq,
			Model model,			
			@RequestParam(required = false) String commentId) {
		
		boardFreeService.getFreeComment(model, seq, commentId);
		
//		return "board/free/comment";
	}
	
	@RequestMapping(value = "/free/comment/count/{seq}", method = RequestMethod.GET)
	public void freeCommentCount(@PathVariable int seq, Model model) {
		
		boardFreeService.getFreeCommentCount(model, seq);
		
	}
	
	@RequestMapping(value = "/comment/like/{seq}")
	public void setFreeCommentLike(@PathVariable int seq, Model model,
			@RequestParam(required = true) String id) {
		
		boardFreeService.setUsersCommentFeelings(model, seq, id, CommonConst.FEELING_TYPE_LIKE);
	}
	
	@RequestMapping(value = "/comment/dislike/{seq}")
	public void setFreeCommentDislike(@PathVariable int seq, Model model,
			@RequestParam(required = true) String id) {
		
		boardFreeService.setUsersCommentFeelings(model, seq, id, CommonConst.FEELING_TYPE_DISLIKE);
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
	
	@RequestMapping(value = "/data/free/top", method = RequestMethod.GET)
	public void dataFreeTopList(Model model) {
		
		Integer status = boardFreeService.getDataFreeTopList(model);
	}		
	
	@RequestMapping(value = "/data/free/comments", method = RequestMethod.GET)
	public void dataFreeCommentsList(Model model,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "20") int size) {
		
		Integer status = boardFreeService.getDataFreeCommentsList(model, page, size);
	}

}
