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

import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.SupporterCount;
import com.jakduk.model.db.LeagueAttendance;
import com.jakduk.repository.LeagueAttendanceRepository;
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
	private LeagueAttendanceRepository leagueAttendanceRepository;
	
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
	
	public Integer getLeagueAttendance(Model model) {
		
		model.addAttribute("kakaoKey", kakaoJavascriptKey);
		
		return HttpServletResponse.SC_OK;
	}	
	
	public void getLeagueAttendanceData(Model model) {
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		
		List<LeagueAttendance> attendances = leagueAttendanceRepository.findAll(sort);
		Stream<LeagueAttendance> sAttendances = attendances.stream();
		Integer totalSum = sAttendances.mapToInt(LeagueAttendance::getTotal).sum();
		sAttendances = attendances.stream();
		Integer gamesSum = sAttendances.mapToInt(LeagueAttendance::getGames).sum();
		
		model.addAttribute("attendances", attendances);
		model.addAttribute("totalSum", totalSum);
		model.addAttribute("gamesSum", gamesSum);
		
	}
}
