package com.jakduk.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.FootballService;
import com.jakduk.core.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

@Controller
@RequestMapping("/stats")
public class StatsController {

	@Value("${kakao.javascript.key}")
	private String kakaoJavascriptKey;

	@Autowired
	private CommonService commonService;

	@Autowired
	private StatsService statsService;

	@Autowired
	private FootballService footballService;

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
	public String supporters(Model model,
			@RequestParam(required = false) String chartType) {

		Integer status = statsService.getSupporters(model, chartType);

		return "stats/supporters";
	}

	@RequestMapping(value = "/attendance", method = RequestMethod.GET)
	public String attendance(Model model) {

		return "redirect:/stats/attendance/league";
	}

	@RequestMapping(value = "/attendance/refresh", method = RequestMethod.GET)
	public String attendanceRefresh() {

		return "redirect:/stats/attendance/league";
	}

	@RequestMapping(value = "/attendance/league/refresh", method = RequestMethod.GET)
	public String attendanceLeagueRefresh() {

		return "redirect:/stats/attendance/league";
	}


	@RequestMapping(value = "/attendance/club/refresh", method = RequestMethod.GET)
	public String attendanceClubRefresh() {

		return "redirect:/stats/attendance/club";
	}
	@RequestMapping(value = "/attendance/season/refresh", method = RequestMethod.GET)
	public String attendanceSeasonRefresh() {

		return "redirect:/stats/attendance/season";
	}

	@RequestMapping(value = "/attendance/league", method = RequestMethod.GET)
	public String attendanceLeague(Model model,
			@RequestParam(required = false) String league) {

		model.addAttribute("kakaoKey", kakaoJavascriptKey);

		if (league != null && !league.isEmpty()) {
			model.addAttribute("league", league);
		}

		return "stats/attendanceLeague";
	}

	@RequestMapping(value = "/attendance/club", method = RequestMethod.GET)
	public String attendanceClub(Model model,
			@RequestParam(required = false) String clubOrigin) {

		model.addAttribute("kakaoKey", kakaoJavascriptKey);

		if (clubOrigin != null && !clubOrigin.isEmpty()) {
			model.addAttribute("clubOrigin", clubOrigin);
		}

		return "stats/attendanceClub";
	}

	@RequestMapping(value = "/attendance/season", method = RequestMethod.GET)
	public String attendanceSeason(Model model,
								   HttpServletRequest request,
								   @RequestParam(required = false, defaultValue = "2015") int season,
								   @RequestParam(required = false, defaultValue = CommonConst.K_LEAGUE_ABBREVIATION) String league) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, null);

		Map<String, String> fcNames = new HashMap<>();

		List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

		for (FootballClub fc : footballClubs) {
			fcNames.put(fc.getOrigin().getId(), fc.getNames().get(0).getShortName());
		}

		model.addAttribute("kakaoKey", kakaoJavascriptKey);

		if (season != 0) {
			model.addAttribute("season", season);
		}

		if (league != null && !league.isEmpty()) {
			model.addAttribute("league", league);
		}

		try {
			model.addAttribute("fcNames", new ObjectMapper().writeValueAsString(fcNames));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.parsing.or.generating"));
		}

		return "stats/attendanceSeason";
	}
}
