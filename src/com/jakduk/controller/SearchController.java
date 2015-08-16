package com.jakduk.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.jakduk.service.SearchService;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 6.
* @desc     :
*/

@Controller
@RequestMapping("/search")
public class SearchController {

	@Resource
	LocaleResolver localeResolver;
	
	@Autowired
	private SearchService searchService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root(HttpServletRequest request, Model model,
			@RequestParam(required = false) String q) {
		
		Locale locale = localeResolver.resolveLocale(request);	
		searchService.getSearchBoard(model, locale, q);
		
		return "search/search";
	}
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public String refreshSearch() {
		
		return "redirect:/search";
	}	
	
	@RequestMapping(value = "/data/board")
	public void dataBoardList(Model model,
			@RequestParam(required = true) String q) {
		
		searchService.searchDataOfBoard(model, q);
		
	}
}
