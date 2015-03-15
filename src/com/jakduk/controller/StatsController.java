package com.jakduk.controller;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.service.CommonService;
import com.jakduk.service.StatsService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

@Controller
@RequestMapping("/stats")
public class StatsController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private StatsService statsService;
	
	@Resource
	LocaleResolver localeResolver;
	
	@RequestMapping
	public String root() {
		
		return "redirect:/stats/supporters";
	}
	
	@RequestMapping(value = "/supporters/refresh", method = RequestMethod.GET)
	public String supportersRefresh() {
		
		return "redirect:/stats/supporters";
	}	
	
	@RequestMapping(value = "/supporters", method = RequestMethod.GET)
	public String supporter(Model model,
			@RequestParam(required = false) String chartType) {
		
		Integer status = statsService.getSupporters(model, chartType);
		
		return "stats/supporters";
	}
	
	@RequestMapping(value = "/data/supporters", method = RequestMethod.GET)
	public void dataSupporter(Model model
			, HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, null);
		statsService.getSupportersData(model, language);
	}

	@RequestMapping(value = "/attendance/refresh", method = RequestMethod.GET)
	public String attendanceRefresh() {
		
		return "redirect:/stats/attendance/league";
	}	
	
	@RequestMapping(value = "/attendance", method = RequestMethod.GET)
	public String attendance(Model model) {
		
		return "redirect:/stats/attendance/league";
	}
	
	@RequestMapping(value = "/attendance/league", method = RequestMethod.GET)
	public String attendanceLeague(Model model) {
		
		Integer status = statsService.getLeagueAttendance(model);
		
		return "stats/attendanceLeague";
	}	
	
	@RequestMapping(value = "/data/attendance/league", method = RequestMethod.GET)
	public void dataLeagueAttendance(Model model,
			@RequestParam(required = false) String league) {
		
		statsService.getLeagueAttendanceData(model, league);
	}

}
