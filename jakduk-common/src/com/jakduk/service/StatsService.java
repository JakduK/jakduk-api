package com.jakduk.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.mapping.Set;
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
		
		model.addAttribute("attendances", attendances);
	}
	
	public Integer getAttendanceClub(Model model, String clubOrigin) {
		
		model.addAttribute("kakaoKey", kakaoJavascriptKey);
		
		if (clubOrigin != null && !clubOrigin.isEmpty()) {
			model.addAttribute("clubOrigin", clubOrigin);
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	public void getAttendanceClubData(Model model, String clubOrigin) {
		
		FootballClubOrigin footballClubOrigin;
		
		if (clubOrigin != null && !clubOrigin.isEmpty()) {
			footballClubOrigin = footballClubOriginRepository.findByName(clubOrigin);
			
			Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
			
			if (footballClubOrigin != null) {
				List<AttendanceClub> attendances = attendanceClubRepository.findByClub(footballClubOrigin, sort);
				Stream<AttendanceClub> sAttendances = attendances.stream();
				Integer totalSum = sAttendances.mapToInt(AttendanceClub::getTotal).sum();
				sAttendances = attendances.stream();
				Integer gamesSum = sAttendances.mapToInt(AttendanceClub::getGames).sum();
				Integer average = 0;
				
				if (totalSum != 0 && gamesSum != 0) {
					average = totalSum / gamesSum;
				}
				
				model.addAttribute("attendances", attendances);
				model.addAttribute("totalSum", totalSum);
				model.addAttribute("gamesSum", gamesSum);
				model.addAttribute("average", average);
			}
		} else {
		}
	}
	
	public Integer getAttendanceSeason(Model model, String language, int season, String league) {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	public void getAttendanceSeasonData(Model model, int season, String league) {
		
		if (league == null) {
			league = CommonConst.K_LEAGUE_ABBREVIATION;
		}
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("average"));
		List<AttendanceClub> attendances = null;
		
		if (league.equals(CommonConst.K_LEAGUE_ABBREVIATION)) {
			attendances = attendanceClubRepository.findBySeason(season, sort);
		} else {
			attendances = attendanceClubRepository.findBySeasonAndLeague(season, league, sort);
		}
		
		model.addAttribute("attendances", attendances);
	}
	
}
