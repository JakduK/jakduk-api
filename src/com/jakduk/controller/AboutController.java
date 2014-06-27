package com.jakduk.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 6. 27.
 * @desc     :
 */

@Controller
@RequestMapping("/about")
public class AboutController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/about/intro";
	}
	
	@RequestMapping(value = "/intro")
	public String intro() {
		
		return "about/intro";
	}

}
