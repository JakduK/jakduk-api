package com.jakduk.api.service;

import com.jakduk.api.common.Constants;
import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.aggregate.SupporterCount;
import com.jakduk.api.model.db.AttendanceClub;
import com.jakduk.api.model.db.AttendanceLeague;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.model.db.FootballClubOrigin;
import com.jakduk.api.repository.AttendanceClubRepository;
import com.jakduk.api.repository.AttendanceLeagueRepository;
import com.jakduk.api.repository.footballclub.FootballClubOriginRepository;
import com.jakduk.api.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


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
	private UserRepository userRepository;

	@Autowired
	private AttendanceLeagueRepository attendanceLeagueRepository;

	@Autowired
	private AttendanceClubRepository attendanceClubRepository;

	@Autowired
	private FootballClubOriginRepository footballClubOriginRepository;

	// 대회별 관중수 목록.
	public List<AttendanceLeague> findLeagueAttendances(Sort sort) {
		return attendanceLeagueRepository.findAll(sort);
	}

	// 대회별 관중수 목록.
	public List<AttendanceLeague> findLeagueAttendances(Competition competition, Sort sort) {
		return attendanceLeagueRepository.findByCompetition(competition, sort);
	}

	// 대회별 관중수 하나.
	public AttendanceLeague findOneById(String id) {
		return attendanceLeagueRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ATTENDANCE_LEAGUE));
	}

	// 새 대회별 관중수 저장.
	public void saveLeagueAttendance(AttendanceLeague attendanceLeague) {

		attendanceLeagueRepository.save(attendanceLeague);
	}

	// 대회별 관중수 하나 지움.
	public void deleteLeagueAttendance(String id) {
		attendanceLeagueRepository.deleteById(id);
	}

	public Map<String, Object> getSupportersData(String language) {

		List<SupporterCount> supporters = jakdukDAO.getSupportFCCount(language);
		Long usersTotal = userRepository.count();

		Stream<SupporterCount> sSupporters = supporters.stream();
		Integer supportersTotal = sSupporters.mapToInt(SupporterCount::getCount).sum();

		Map<String, Object> data = new HashMap<>();
		data.put("supporters", supporters);
		data.put("supportersTotal", supportersTotal);
		data.put("usersTotal", usersTotal.intValue());
		return data;
	}

	public AttendanceClub findAttendanceClubById(String id) {

		return attendanceClubRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ATTENDANCE_CLUB));
	}

	public List<AttendanceClub> getAttendanceClub(String clubOrigin) {

		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOneByName(clubOrigin)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB_ORIGIN));

		Sort sort = Sort.by(Sort.Direction.ASC, "season", "league");

		return attendanceClubRepository.findByClub(footballClubOrigin, sort);
	}

	public List<AttendanceClub> getAttendancesSeason(Integer season, String league) {

		Sort sort = Sort.by(Sort.Direction.DESC, "average");
		List<AttendanceClub> attendances = null;

		if (league.equals(Constants.K_LEAGUE_ABBREVIATION)) {
			attendances = attendanceClubRepository.findBySeason(season, sort);
		} else {
			attendances = attendanceClubRepository.findBySeasonAndLeague(season, league, sort);
		}

		return attendances;
	}

}
