package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.*;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.admin.*;
import com.jakduk.api.service.AdminService;
import com.jakduk.api.service.CommonService;
import com.jakduk.api.service.CompetitionService;
import com.jakduk.api.service.StatsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.*;

/**
 * 관리자 API
 *
 * @author pyohwan
 *         16. 5. 8 오후 11:26
 */

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CommonService commonService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private StatsService statsService;

	@Autowired
	private CompetitionService competitionService;

	// 알림판 목록
	@RequestMapping(value = "/home/descriptions", method = RequestMethod.GET)
	public Map<String, Object> getHomeDescriptions() {

		List<HomeDescription> homeDescriptions = adminService.findHomeDescriptions();

		Map<String, Object> response = new HashMap<>();

		response.put("homeDescriptions", homeDescriptions);

		return response;
	}

	// 알림판 하나
	@RequestMapping(value = "/home/description/{id}", method = RequestMethod.GET)
	public Map<String, Object> getHomeDescription(@PathVariable String id) {

		HomeDescription homeDescription = adminService.findHomeDescriptionById(id);

		if (Objects.isNull(homeDescription)) {
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");
		}

		Map<String, Object> response = new HashMap<>();

		response.put("homeDescription", homeDescription);

		return response;
	}

	// 새 알림판 저장
	@RequestMapping(value = "/home/description", method = RequestMethod.POST)
	public Map<String, Object> addHomeDescription(@RequestBody HomeDescriptionRequest request) {

		if (Objects.isNull(request.getDesc()) || request.getDesc().isEmpty() == true)
			throw new IllegalArgumentException("desc는 필수값입니다.");

		if (Objects.isNull(request.getPriority()))
			throw new IllegalArgumentException("priority는 필수값입니다.");

		HomeDescription homeDescription = new HomeDescription();
		BeanUtils.copyProperties(request, homeDescription);

		adminService.saveHomeDescription(homeDescription);

		Map<String, Object> response = new HashMap();
		response.put("homeDescription", homeDescription);

		return response;
	}

	// 알림판 편집
	@RequestMapping(value = "/home/description/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editHomeDescription(@PathVariable String id,
		@RequestBody HomeDescriptionRequest request) {

		if (Objects.isNull(request.getDesc()) || request.getDesc().isEmpty() == true)
			throw new IllegalArgumentException("desc는 필수값입니다.");

		if (Objects.isNull(request.getPriority()))
			throw new IllegalArgumentException("priority는 필수값입니다.");

		HomeDescription existHomeDescription = adminService.findHomeDescriptionById(id);

		if (Objects.isNull(existHomeDescription))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");

		HomeDescription homeDescription = new HomeDescription();
		BeanUtils.copyProperties(request, homeDescription);
		homeDescription.setId(id);

		adminService.saveHomeDescription(homeDescription);

		Map<String, Object> response = new HashMap<>();

		response.put("homeDescription", homeDescription);

		return response;
	}

	// 알림판 지움
	@RequestMapping(value = "/home/description/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> deleteHomeDescription(@PathVariable String id) {

		HomeDescription existHomeDescription = adminService.findHomeDescriptionById(id);

		if (Objects.isNull(existHomeDescription))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");

		adminService.deleteHomeDescriptionById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("result", true);

		return response;
	}

	// 백과사전 목록
	@RequestMapping(value = "/encyclopedias", method = RequestMethod.GET)
	public Map<String, Object> getEncyclopedias() {

		List<Encyclopedia> encyclopedias = adminService.findEncyclopedias();

		Map<String, Object> response = new HashMap<>();

		response.put("encyclopedias", encyclopedias);

		return response;
	}

	// 백과사전 하나
	@RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.GET)
	public Map<String, Object> getEncyclopedia(@PathVariable String id) {

		Encyclopedia encyclopedia = adminService.findEncyclopediaById(id);

		if (Objects.isNull(encyclopedia))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

		Map<String, Object> response = new HashMap<>();
		response.put("encyclopedia", encyclopedia);
		return response;
	}

	// 새 백과사전 저장
	@RequestMapping(value = "/encyclopedia", method = RequestMethod.POST)
	public Map<String, Object> addEncyclopedia(@RequestBody Encyclopedia encyclopedia) {

		// 신규로 만들기때문에 null로 설정.
		encyclopedia.setId(null);

		if (encyclopedia.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			encyclopedia.setSeq(commonService.getNextSequence(Constants.ENCYCLOPEDIA_EN));
		} else if (encyclopedia.getLanguage().equals(Locale.KOREAN.getLanguage())) {
			encyclopedia.setSeq(commonService.getNextSequence(Constants.ENCYCLOPEDIA_KO));
		}

		adminService.saveEncyclopedia(encyclopedia);

		Map<String, Object> response = new HashMap<>();
		response.put("encyclopedia", encyclopedia);

		return response;
	}

	// 백과사전 편집
	@RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editEncyclopedia(@PathVariable String id,
		@RequestBody Encyclopedia encyclopedia) {

		Encyclopedia existEncyclopedia = adminService.findEncyclopediaById(id);

		if (Objects.isNull(existEncyclopedia))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

		encyclopedia.setId(id);
		adminService.saveEncyclopedia(encyclopedia);

		Map<String, Object> response = new HashMap<>();
		response.put("encyclopedia", encyclopedia);

		return response;
	}

	// 백과사전 지움
	@RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> deleteEncyclopedia(@PathVariable String id) {

		Encyclopedia encyclopedia = adminService.findEncyclopediaById(id);

		if (Objects.isNull(encyclopedia))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

		adminService.deleteEncyclopediaById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("result", true);

		return response;
	}

	// 부모 축구단 목록
	@RequestMapping(value = "/origin/football/clubs", method = RequestMethod.GET)
	public Map<String, Object> getOriginFootballClubs() {

		List<FootballClubOrigin> fcOrigins = adminService.findOriginFootballClubs();

		Map<String, Object> response = new HashMap<>();
		response.put("originFCs", fcOrigins);

		return response;
	}

	// 부모 축구단 하나
	@RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.GET)
	public Map<String, Object> getOriginFootballClub(@PathVariable String id) {

		FootballClubOrigin footballClubOrigin = adminService.findOriginFootballClubById(id);

		if (Objects.isNull(footballClubOrigin))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

		Map<String, Object> response = new HashMap<>();
		response.put("originFC", footballClubOrigin);

		return response;
	}

	// 새 부모 축구단 하나 저장
	@RequestMapping(value = "/origin/football/club", method = RequestMethod.POST)
	public Map<String, Object> addOriginFootballClub(@RequestBody FootballClubOrigin footballClubOrigin) {

		// 신규로 만들기때문에 null로 설정.
		footballClubOrigin.setId(null);

		adminService.saveOriginFootballClub(footballClubOrigin);

		Map<String, Object> response = new HashMap<>();
		response.put("originFC", footballClubOrigin);

		return response;
	}

	// 부모 축구단 하나 편집
	@RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editOriginFootballClub(@PathVariable String id,
		@RequestBody FootballClubOrigin footballClubOrigin) {

		FootballClubOrigin existFootballClubOrigin = adminService.findOriginFootballClubById(id);

		if (Objects.isNull(existFootballClubOrigin))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

		footballClubOrigin.setId(id);
		adminService.saveOriginFootballClub(footballClubOrigin);

		Map<String, Object> response = new HashMap<>();
		response.put("originFC", footballClubOrigin);

		return response;
	}

	// 부모 축구단 하나 지움
	@RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> deleteOriginFootballClub(@PathVariable String id) {

		FootballClubOrigin existFootballClubOrigin = adminService.findOriginFootballClubById(id);

		if (Objects.isNull(existFootballClubOrigin))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

		adminService.deleteOriginFootballClub(id);

		Map<String, Object> response = new HashMap<>();
		response.put("result", true);

		return response;
	}

	// 축구단 목록
	@RequestMapping(value = "/football/clubs", method = RequestMethod.GET)
	public Map<String, Object> getFootballClubs() {

		List<FootballClub> footballClubs = adminService.findFootballClubs();

		Map<String, Object> response = new HashMap<>();
		response.put("fcs", footballClubs);

		return response;
	}

	// 축구단 하나
	@RequestMapping(value = "/football/club/{id}", method = RequestMethod.GET)
	public Map<String, Object> getFootballClub(@PathVariable String id) {

		FootballClub fc = adminService.findFootballClubById(id);

		if (Objects.isNull(fc))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 축구단이 존재하지 않습니다.");

		List<FootballClubOrigin> originFCs = adminService.findOriginFootballClubs();

		FootballClubRequest fcRequest = new FootballClubRequest();
		fcRequest.setId(fc.getId());
		fcRequest.setActive(fc.getActive());
		fcRequest.setOrigin(fc.getOrigin().getId());

		for (LocalName fcName : fc.getNames()) {
			if (fcName.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				fcRequest.setFullNameKr(fcName.getFullName());
				fcRequest.setShortNameKr(fcName.getShortName());
			} else if (fcName.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				fcRequest.setFullNameEn(fcName.getFullName());
				fcRequest.setShortNameEn(fcName.getShortName());
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("originFCs", originFCs);
		response.put("fcRequest", fcRequest);

		return response;
	}

	// 새 축구단 하나 저장
	@RequestMapping(value = "/football/club", method = RequestMethod.POST)
	public Map<String, Object> addFootballClub(@RequestBody FootballClubRequest request) {

		if (Objects.isNull(request.getOrigin()) || request.getOrigin().isEmpty())
			throw new IllegalArgumentException("origin은 필수값입니다.");

		FootballClub footballClub = buildFootballClub(null, request);
		adminService.saveFootballClub(footballClub);

		Map<String, Object> response = new HashMap<>();
		response.put("fc", footballClub);

		return response;
	}

	// 축구단 편집
	@RequestMapping(value = "/football/club/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editFootballClub(@PathVariable String id, @RequestBody FootballClubRequest request) {
		FootballClub footballClub = buildFootballClub(id, request);
		adminService.saveFootballClub(footballClub);

		Map<String, Object> response = new HashMap<>();
		response.put("fc", footballClub);

		return response;
	}

	// 대회별 관중수 목록
	@RequestMapping(value = "/league/attendances", method = RequestMethod.GET)
	public Map<String, Object> getLeagueAttendances(@RequestParam(required = false) String competitionId,
		@RequestParam(required = false) String competitionCode) {

		Competition competition = null;
		List<AttendanceLeague> leagueAttendances;

		if (Objects.nonNull(competitionId)) {
			competition = competitionService.findOneById(competitionId);
		} else if (Objects.nonNull(competitionCode)) {
			competition = competitionService.findCompetitionByCode(competitionCode);
		}

		if (Objects.isNull(competition)) {
			leagueAttendances = statsService.findLeagueAttendances(Constants.SORT_BY_ID_ASC);
		} else {
			leagueAttendances = statsService.findLeagueAttendances(competition, Constants.SORT_BY_ID_ASC);
		}

		List<Competition> competitions = competitionService.findCompetitions();

		Map<String, Object> response = new HashMap<>();
		response.put("leagueAttendances", leagueAttendances);
		response.put("competitions", competitions);

		return response;
	}

	// 대회별 관중수 하나
	@RequestMapping(value = "/league/attendance/{id}", method = RequestMethod.GET)
	public Map<String, Object> getLeagueAttendance(@PathVariable String id) {

		AttendanceLeague attendanceLeague = statsService.findOneById(id);

		List<Competition> competitions = competitionService.findCompetitions();

		Map<String, Object> response = new HashMap<>();
		response.put("leagueAttendance", attendanceLeague);
		response.put("competitions", competitions);

		return response;
	}

	// 새 대회별 관중수 하나 저장
	@RequestMapping(value = "/league/attendance", method = RequestMethod.POST)
	public EmptyJsonResponse addLeagueAttendance(@Valid @RequestBody LeagueAttendanceForm form) {

		Competition competition = competitionService.findOneById(form.getCompetitionId());

		AttendanceLeague attendanceLeague = new AttendanceLeague();
		BeanUtils.copyProperties(form, attendanceLeague);
		attendanceLeague.setId(null);
		attendanceLeague.setCompetition(competition);

		statsService.saveLeagueAttendance(attendanceLeague);

		return EmptyJsonResponse.newInstance();

	}

	// 대회별 관중수 하나 편집
	@RequestMapping(value = "/league/attendance/{id}", method = RequestMethod.PUT)
	public EmptyJsonResponse editLeagueAttendance(@PathVariable String id,
		@Valid @RequestBody LeagueAttendanceForm form) {

		// 존재 확인
		statsService.findOneById(id);

		Competition competition = competitionService.findOneById(form.getCompetitionId());

		AttendanceLeague attendanceLeague = new AttendanceLeague();
		BeanUtils.copyProperties(form, attendanceLeague);
		attendanceLeague.setId(id);
		attendanceLeague.setCompetition(competition);

		statsService.saveLeagueAttendance(attendanceLeague);

		return EmptyJsonResponse.newInstance();
	}

	// 대회별 관중수 하나 지움
	@RequestMapping(value = "/league/attendance/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteLeagueAttendance(@PathVariable String id) {

		// 존재 확인
		statsService.findOneById(id);

		statsService.deleteLeagueAttendance(id);

		return EmptyJsonResponse.newInstance();
	}

	// 클럽 관중수 목록
	@RequestMapping(value = "/club/attendances", method = RequestMethod.GET)
	public Map<String, Object> getAttendanceClubs() {

		Map<String, Object> data = new HashMap<>();
		data.put("attendanceClubs", adminService.getAttendanceClubList());

		return data;
	}

	// 클럽 관중수 하나
	@RequestMapping(value = "/club/attendance/{id}", method = RequestMethod.GET)
	public Map<String, Object> getAttendanceClub(@PathVariable String id) {

		AttendanceClub attendanceClub = statsService.findAttendanceClubById(id);

		AttendanceClubForm attendanceClubForm = new AttendanceClubForm();
		BeanUtils.copyProperties(attendanceClub, attendanceClubForm);

		if (!ObjectUtils.isEmpty(attendanceClub.getClub()))
			attendanceClubForm.setOrigin(attendanceClub.getClub().getId());

		Map<String, Object> response = new HashMap<>();
		response.put("attendanceClubWrite", attendanceClubForm);

		return response;
	}

	// 클럽 관중수 변경
	@RequestMapping(value = "/club/attendance/{id}", method = RequestMethod.PUT)
	public EmptyJsonResponse addAttendanceClub(@PathVariable String id,
		@Valid @RequestBody AttendanceClubForm attendanceClubWrite) {

		adminService.saveAttendanceClub(
			id, attendanceClubWrite.getOrigin(), attendanceClubWrite.getLeague(),
			attendanceClubWrite.getSeason(), attendanceClubWrite.getGames(), attendanceClubWrite.getTotal(),
			attendanceClubWrite.getAverage()
		);

		return EmptyJsonResponse.newInstance();
	}

	// 클럽 관중수 추가
	@RequestMapping(value = "/club/attendance", method = RequestMethod.POST)
	public EmptyJsonResponse editAttendanceClub(@Valid @RequestBody AttendanceClubForm attendanceClubWrite) {

		adminService.saveAttendanceClub(
			null, attendanceClubWrite.getOrigin(), attendanceClubWrite.getLeague(),
			attendanceClubWrite.getSeason(), attendanceClubWrite.getGames(), attendanceClubWrite.getTotal(),
			attendanceClubWrite.getAverage()
		);

		return EmptyJsonResponse.newInstance();
	}

	@RequestMapping(value = "/thumbnail/size", method = RequestMethod.GET)
	public Map<String, Object> thumbnailSizeWrite() {

		Map<String, Object> data = new HashMap<>();
		data.put("resWidth", Constants.GALLERY_THUMBNAIL_SIZE_WIDTH);
		data.put("resHeight", Constants.GALLERY_THUMBNAIL_SIZE_HEIGHT);

		return data;
	}

	// 썸네일 크기 지정
	@RequestMapping(value = "/thumbnail/size", method = RequestMethod.POST)
	public EmptyJsonResponse thumbnailSizeWrite(@RequestBody ThumbnailSizeWrite thumbnailSizeWrite) {
		adminService.thumbnailSizeWrite(thumbnailSizeWrite);
		return EmptyJsonResponse.newInstance();
	}

	// 경기 목록
	@RequestMapping(value = "/competitions", method = RequestMethod.GET)
	public Map<String, Object> getCompetitions() {
		Map<String, Object> data = new HashMap<>();
		data.put("competitions", adminService.getCompetitions());
		return data;
	}

	// 경기 하나
	@RequestMapping(value = "/competition/{id}", method = RequestMethod.GET)
	public CompetitionWrite getCompetition(@PathVariable String id) {
		return adminService.getCompetition(id);
	}

	// 경기 추가
	@RequestMapping(value = "/competition", method = RequestMethod.POST)
	public CompetitionWrite addCompetition(@RequestBody CompetitionWrite competitionWrite) {
		String id = adminService.writeCompetition(null, competitionWrite).getId();
		return adminService.getCompetition(id);
	}

	// 경기 수정
	@RequestMapping(value = "/competition/{id}", method = RequestMethod.PUT)
	public EmptyJsonResponse editCompetition(@PathVariable String id, @RequestBody CompetitionWrite competitionWrite) {
		adminService.writeCompetition(id, competitionWrite);
		return EmptyJsonResponse.newInstance();
	}

	// 경기 삭제
	@RequestMapping(value = "/competition/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteCompetition(@PathVariable String id) {
		adminService.deleteCompetition(id);
		return EmptyJsonResponse.newInstance();
	}

	// 작두 하나
	@RequestMapping(value = "/jakdu/schedule/{id}", method = RequestMethod.GET)
	public JakduScheduleWrite addJakduSchedule(@PathVariable String id) {
		return adminService.getJakduScheduleWrite(id);
	}

	// 작두 추가
	@RequestMapping(value = "/jakdu/schedule", method = RequestMethod.POST)
	public JakduSchedule jakduScheduleWrite(@RequestBody JakduScheduleWrite jakduScheduleWrite) {
		return adminService.writeJakduSchedule(null, jakduScheduleWrite);
	}

	// 작두 수정
	@RequestMapping(value = "/jakdu/schedule", method = RequestMethod.PUT)
	public EmptyJsonResponse editJakduSchedule(@PathVariable String id,
		@RequestBody JakduScheduleWrite jakduScheduleWrite) {
		adminService.writeJakduSchedule(id, jakduScheduleWrite);
		return EmptyJsonResponse.newInstance();
	}

	// 작두 삭제
	@RequestMapping(value = "/jakdu/schedule/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteSchedule(@PathVariable String id) {
		adminService.deleteJakduSchedule(id);
		return EmptyJsonResponse.newInstance();
	}

	// 작두 목록
	@RequestMapping(value = "/jakdu/schedules", method = RequestMethod.GET)
	public Map<String, Object> jakduSchedules() {
		Map<String, Object> data = new HashMap<>();
		data.put("jakduSchedules", adminService.getDataJakduScheduleList());
		return data;
	}

	// 작두그룹 하나
	@RequestMapping(value = "/jakdu/schedule/group/{id}", method = RequestMethod.GET)
	public JakduScheduleGroupWrite getJakduScheduleGroup(@PathVariable String id) {
		return adminService.getJakduScheduleGroupWrite(id);
	}

	// 작두그룹 추가
	@RequestMapping(value = "/jakdu/schedule/group", method = RequestMethod.POST)
	public JakduScheduleGroup addJakduScheduleGroup(@RequestBody JakduScheduleGroupWrite jakduScheduleGroupWrite) {
		return adminService.writeJakduScheduleGroup(null, jakduScheduleGroupWrite);
	}

	// 작두그룹 수정
	@RequestMapping(value = "/jakdu/schedule/group", method = RequestMethod.PUT)
	public JakduScheduleGroup editJakduScheduleGroup(@PathVariable String id,
		@RequestBody JakduScheduleGroupWrite jakduScheduleGroupWrite) {
		return adminService.writeJakduScheduleGroup(id, jakduScheduleGroupWrite);
	}

	// 작두그룹 삭제
	@RequestMapping(value = "jakdu/schedule/group/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteJakduScheduleGroup(@PathVariable String id) {
		adminService.deleteJakduScheduleGroup(id);
		return EmptyJsonResponse.newInstance();
	}

	// 작두그룹 목록
	@RequestMapping(value = "/jakdu/schedule/groups", method = RequestMethod.GET)
	public Map<String, Object> jakduScheduleGroups() {
		Map<String, Object> data = new HashMap<>();
		data.put("jakduScheduleGroups", adminService.getDataJakduScheduleGroupList());
		return data;
	}

	private FootballClub buildFootballClub(String id, FootballClubRequest request) {
		FootballClubOrigin footballClubOrigin = adminService.findOriginFootballClubById(request.getOrigin());

		if (Objects.isNull(footballClubOrigin))
			throw new IllegalArgumentException("id가 " + request.getOrigin() + "에 해당하는 부모 축구단이 존재하지 않습니다.");

		LocalName footballClubNameKr = new LocalName(Locale.KOREAN.getLanguage(), request.getFullNameKr(),
			request.getShortNameKr());
		LocalName footballClubNameEn = new LocalName(Locale.ENGLISH.getLanguage(), request.getFullNameEn(),
			request.getShortNameEn());

		ArrayList<LocalName> names = new ArrayList<>();
		names.add(footballClubNameKr);
		names.add(footballClubNameEn);

		FootballClub footballClub = new FootballClub();
		footballClub.setId(id);
		footballClub.setActive(request.getActive());
		footballClub.setOrigin(footballClubOrigin);
		footballClub.setNames(names);

		return footballClub;
	}

}
