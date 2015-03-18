package com.jakduk.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.SupporterCount;
import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.repository.AttendanceLeagueRepository;
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
	private UserRepository userRepository;
	
	@Autowired
	private AttendanceLeagueRepository attendanceLeagueRepository;
	
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
	
	public Integer getAttendanceLeague(Model model, String league) {
		
		model.addAttribute("kakaoKey", kakaoJavascriptKey);
		
		if (league != null && !league.isEmpty()) {
			model.addAttribute("league", league);
		}
		
		return HttpServletResponse.SC_OK;
	}	
	
	public void getAttendanceLeagueData(Model model, String league) {
		
		if (league == null) {
			league = CommonConst.K_LEAGUE_ABBREVIATION;
		}
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		
		List<AttendanceLeague> attendances = attendanceLeagueRepository.findByLeague(league, sort);
		Stream<AttendanceLeague> sAttendances = attendances.stream();
		Integer totalSum = sAttendances.mapToInt(AttendanceLeague::getTotal).sum();
		sAttendances = attendances.stream();
		Integer gamesSum = sAttendances.mapToInt(AttendanceLeague::getGames).sum();
		
		model.addAttribute("attendances", attendances);
		model.addAttribute("totalSum", totalSum);
		model.addAttribute("gamesSum", gamesSum);
		
	}
}
