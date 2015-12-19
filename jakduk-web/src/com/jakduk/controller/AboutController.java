package com.jakduk.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.service.CommonService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 6. 27.
 * @desc     :
 */

@Controller
@RequestMapping("/about")
public class AboutController {

	@Autowired
	private CommonService commonService;
	
	@Resource
	LocaleResolver localeResolver;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/about/intro";
	}

	@RequestMapping(value = "/intro/refresh", method = RequestMethod.GET)
	public String refreshIntro() {
		
		return "redirect:/about/intro";
	}		
	
	@RequestMapping(value = "/intro")
	public String intro(HttpServletRequest request, HttpServletResponse response,
			Model model,
			@RequestParam(required = false) String lang) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		model.addAttribute("lang", language);
		
		return "about/intro";
	}

}
