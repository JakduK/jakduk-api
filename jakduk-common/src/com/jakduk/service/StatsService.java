package com.jakduk.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.model.web.stats.AttendanceClubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.AttendanceClub;
import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.etc.SupporterCount;
import com.jakduk.repository.AttendanceClubRepository;
import com.jakduk.repository.AttendanceLeagueRepository;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.repository.UserRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

@Service
public class StatsService {
	
	@Value("${kakao.javascript.key}")
	private String kakaoJavascriptKey;
	
	@Autowired
	private JakdukDAO jakdukDAO;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AttendanceLeagueRepository attendanceLeagueRepository;
	
	@Autowired
	private AttendanceClubRepository attendanceClubRepository;
	
	@Autowired
	private FootballClubOriginRepository footballClubOriginRepository;
	
	public Integer getSupporters(Model model, String chartType) {
		
		model.addAttribute("kakaoKey", kakaoJavascriptKey);
		
		if (chartType != null && !chartType.isEmpty()) {
			model.addAttribute("chartType", chartType);
		}
		
		return HttpServletResponse.SC_OK;
	}

	public void getSupportersData(Model model, String language) {
		
		List<SupporterCount> supporters = jakdukDAO.getSupportFCCount(language);
		Long usersTotal = userRepository.count();
		
		Stream<SupporterCount> sSupporters = supporters.stream();
		Integer supportersTotal = sSupporters.mapToInt(SupporterCount::getCount).sum();
		
		model.addAttribute("supporters", supporters);
		model.addAttribute("supportersTotal", supportersTotal);
		model.addAttribute("usersTotal", usersTotal.intValue());
	}

	public List<AttendanceLeague> getAttendanceLeague(String league) {
		
		if (Objects.isNull(league)) {
			league = CommonConst.K_LEAGUE_ABBREVIATION;
		}
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		
		List<AttendanceLeague> attendances = attendanceLeagueRepository.findByLeague(league, sort);
		
		return attendances;
	}
	
	public List<AttendanceClub> getAttendanceClub(Locale locale, String clubOrigin) {

		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findByName(clubOrigin);

		if (Objects.isNull(footballClubOrigin))
			throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "stats.msg.not.found.football.origin.exception"));

		AttendanceClubResponse response = new AttendanceClubResponse();

		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));

		List<AttendanceClub> attendances = attendanceClubRepository.findByClub(footballClubOrigin, sort);

		return attendances;
	}
	
	public void getAttendancesSeason(Locale locale, Model model, String language, int season, String league) {
		Map<String, String> fcNames = new HashMap<>();

		List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

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
	}
	
	public List<AttendanceClub> getAttendancesSeason(Integer season, String league) {

		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("average"));
		List<AttendanceClub> attendances = null;
		
		if (league.equals(CommonConst.K_LEAGUE_ABBREVIATION)) {
			attendances = attendanceClubRepository.findBySeason(season, sort);
		} else {
			attendances = attendanceClubRepository.findBySeasonAndLeague(season, league, sort);
		}
		
		return attendances;
	}
	
}
