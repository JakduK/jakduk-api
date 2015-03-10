package com.jakduk.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.SupporterCount;
import com.jakduk.model.db.LeagueAttendance;
import com.jakduk.repository.LeagueAttendanceRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

@Service
public class StatsService {
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired
	private LeagueAttendanceRepository leagueAttendanceRepository;

	public void getSupporter(Model model, String language) {
		
		List<SupporterCount> supporters = jakdukDAO.getSupportFCCount(language);
		
		model.addAttribute("supporters", supporters);
		
	}
	
	public void getLeagueAttendance(Model model) {
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		
		List<LeagueAttendance> attendances = leagueAttendanceRepository.findAll(sort);
		
		model.addAttribute("attendances", attendances);
		
	}
}
