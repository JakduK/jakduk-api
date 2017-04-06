package com.jakduk.api.restcontroller.admin;


import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.admin.vo.AttendanceClubForm;
import com.jakduk.api.restcontroller.admin.vo.FootballClubRequest;
import com.jakduk.api.restcontroller.admin.vo.HomeDescriptionRequest;
import com.jakduk.api.restcontroller.admin.vo.LeagueAttendanceForm;
import com.jakduk.api.service.search.SearchService;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.*;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.embedded.LocalSimpleName;
import com.jakduk.core.model.web.CompetitionWrite;
import com.jakduk.core.model.web.ThumbnailSizeWrite;
import com.jakduk.core.model.web.board.BoardCategoryWrite;
import com.jakduk.core.model.web.jakdu.JakduScheduleGroupWrite;
import com.jakduk.core.model.web.jakdu.JakduScheduleWrite;
import com.jakduk.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author pyohwan
 *         16. 5. 8 오후 11:26
 */

@Slf4j
@Api(tags = "Admin", description = "관리자 API")
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

	@Autowired
	private CommonService commonService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private StatsService statsService;

	@Autowired
	private CompetitionService competitionService;

	@Autowired
	private SearchService searchService;

	@Autowired
	private BoardCategoryService boardCategoryService;

	@ApiOperation(value = "알림판 목록")
	@RequestMapping(value = "/home/descriptions", method = RequestMethod.GET)
	public Map<String, Object> getHomeDescriptions() {

		List<HomeDescription> homeDescriptions = adminService.findHomeDescriptions();

		Map<String, Object> response = new HashMap<>();

		response.put("homeDescriptions", homeDescriptions);

		return response;
	}

	@ApiOperation(value = "알림판 하나")
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

	@ApiOperation(value = "새 알림판 저장")
	@RequestMapping(value = "/home/description", method = RequestMethod.POST)
	public Map<String, Object> addHomeDescription(@RequestBody HomeDescriptionRequest homeDescriptionRequest) {

		if (Objects.isNull(homeDescriptionRequest.getDesc()) || homeDescriptionRequest.getDesc().isEmpty() == true)
			throw new IllegalArgumentException("desc는 필수값입니다.");

		if (Objects.isNull(homeDescriptionRequest.getPriority()))
			throw new IllegalArgumentException("priority는 필수값입니다.");

		HomeDescription homeDescription = HomeDescription.builder()
			.desc(homeDescriptionRequest.getDesc())
			.priority(homeDescriptionRequest.getPriority())
			.build();

		adminService.saveHomeDescription(homeDescription);

		Map<String, Object> response = new HashMap();

		response.put("homeDescription", homeDescription);

		return response;
	}

	@ApiOperation(value = "알림판 편집")
	@RequestMapping(value = "/home/description/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editHomeDescription(@PathVariable String id,
	                                               @RequestBody HomeDescriptionRequest homeDescriptionRequest) {

		if (Objects.isNull(homeDescriptionRequest.getDesc()) || homeDescriptionRequest.getDesc().isEmpty() == true)
			throw new IllegalArgumentException("desc는 필수값입니다.");

		if (Objects.isNull(homeDescriptionRequest.getPriority()))
			throw new IllegalArgumentException("priority는 필수값입니다.");

		HomeDescription existHomeDescription = adminService.findHomeDescriptionById(id);

		if (Objects.isNull(existHomeDescription))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");

		HomeDescription homeDescription = HomeDescription.builder()
			.id(id)
			.desc(homeDescriptionRequest.getDesc())
			.priority(homeDescriptionRequest.getPriority())
			.build();

		adminService.saveHomeDescription(homeDescription);

		Map<String, Object> response = new HashMap<>();

		response.put("homeDescription", homeDescription);

		return response;
	}

	@ApiOperation(value = "알림판 지움")
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

	@ApiOperation(value = "백과사전 목록")
	@RequestMapping(value = "/encyclopedias", method = RequestMethod.GET)
	public Map<String, Object> getEncyclopedias() {

		List<Encyclopedia> encyclopedias = adminService.findEncyclopedias();

		Map<String, Object> response = new HashMap<>();

		response.put("encyclopedias", encyclopedias);

		return response;
	}

	@ApiOperation(value = "백과사전 하나")
	@RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.GET)
	public Map<String, Object> getEncyclopedia(@PathVariable String id) {

		Encyclopedia encyclopedia = adminService.findEncyclopediaById(id);

		if (Objects.isNull(encyclopedia))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

		Map<String, Object> response = new HashMap<>();
		response.put("encyclopedia", encyclopedia);
		return response;
	}

	@ApiOperation(value = "새 백과사전 저장")
	@RequestMapping(value = "/encyclopedia", method = RequestMethod.POST)
	public Map<String, Object> addEncyclopedia(@RequestBody Encyclopedia encyclopedia) {

		// 신규로 만들기때문에 null로 설정.
		encyclopedia.setId(null);

		if (encyclopedia.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			encyclopedia.setSeq(commonService.getNextSequence(CoreConst.ENCYCLOPEDIA_EN));
		} else if (encyclopedia.getLanguage().equals(Locale.KOREAN.getLanguage())) {
			encyclopedia.setSeq(commonService.getNextSequence(CoreConst.ENCYCLOPEDIA_KO));
		}

		adminService.saveEncyclopedia(encyclopedia);

		Map<String, Object> response = new HashMap<>();
		response.put("encyclopedia", encyclopedia);

		return response;
	}

	@ApiOperation(value = "백과사전 편집")
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

	@ApiOperation(value = "백과사전 지움")
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

	@ApiOperation(value = "부모 축구단 목록")
	@RequestMapping(value = "/origin/football/clubs", method = RequestMethod.GET)
	public Map<String, Object> getOriginFootballClubs() {

		List<FootballClubOrigin> fcOrigins = adminService.findOriginFootballClubs();

		Map<String, Object> response = new HashMap<>();
		response.put("originFCs", fcOrigins);

		return response;
	}

	@ApiOperation(value = "부모 축구단 하나")
	@RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.GET)
	public Map<String, Object> getOriginFootballClub(@PathVariable String id) {

		FootballClubOrigin footballClubOrigin = adminService.findOriginFootballClubById(id);

		if (Objects.isNull(footballClubOrigin))
			throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

		Map<String, Object> response = new HashMap<>();
		response.put("originFC", footballClubOrigin);

		return response;
	}

	@ApiOperation(value = "새 부모 축구단 하나 저장")
	@RequestMapping(value = "/origin/football/club", method = RequestMethod.POST)
	public Map<String, Object> addOriginFootballClub(@RequestBody FootballClubOrigin footballClubOrigin) {

		// 신규로 만들기때문에 null로 설정.
		footballClubOrigin.setId(null);

		adminService.saveOriginFootballClub(footballClubOrigin);

		Map<String, Object> response = new HashMap<>();
		response.put("originFC", footballClubOrigin);

		return response;
	}

	@ApiOperation(value = "부모 축구단 하나 편집")
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

	@ApiOperation(value = "부모 축구단 하나 지움")
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

	@ApiOperation(value = "축구단 목록")
	@RequestMapping(value = "/football/clubs", method = RequestMethod.GET)
	public Map<String, Object> getFootballClubs() {

		List<FootballClub> footballClubs = adminService.findFootballClubs();

		Map<String, Object> response = new HashMap<>();
		response.put("fcs", footballClubs);

		return response;
	}

	@ApiOperation(value = "축구단 하나")
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

	@ApiOperation(value = "새 축구단 하나 저장")
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

	@ApiOperation(value = "축구단 편집")
	@RequestMapping(value = "/football/club/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editFootballClub(@PathVariable String id, @RequestBody FootballClubRequest request) {
		FootballClub footballClub = buildFootballClub(id, request);
		adminService.saveFootballClub(footballClub);

		Map<String, Object> response = new HashMap<>();
		response.put("fc", footballClub);

		return response;
	}

	@ApiOperation(value = "게시판 말머리 목록")
	@RequestMapping(value = "/board/categories", method = RequestMethod.GET)
	public Map<String, Object> getBoardCategories() {
		Map<String, Object> response = new HashMap<>();
		response.put("boardCategories", adminService.getBoardCategoryList());

		return response;
	}

	@ApiOperation(value = "게시판 말머리 하나")
	@RequestMapping(value = "/board/category/{id}", method = RequestMethod.GET)
	public Map<String, Object> getBoardCategory(@PathVariable String id) {
		BoardCategoryWrite boardCategoryWrite = adminService.getBoardCategory(id);
		if (Objects.isNull(boardCategoryWrite)) {
			throw new IllegalArgumentException("유효하지 않은 id입니다.");
		}

		Map<String, Object> response = new HashMap<>();
		response.put("boardCategory", boardCategoryWrite);

		return response;
	}

	@ApiOperation(value = "게시판 말머리 편집")
	@RequestMapping(value = "/board/category/write/{id}", method = RequestMethod.PUT)
	public Map<String, Object> editBoardCategory(
		@PathVariable String id, @RequestBody BoardCategory boardCategory) {

		BoardCategoryWrite boardCategoryWrite = adminService.getBoardCategory(id);
		boardCategoryWrite.setCode(boardCategory.getCode());

		for (LocalSimpleName fcName : boardCategory.getNames()) {
			if (fcName.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				boardCategoryWrite.setNameKr(fcName.getName());
			} else if (fcName.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				boardCategoryWrite.setNameEn(fcName.getName());
			}
		}

		Map<String, Object> response = new HashMap<>();
		response.put("boardCategory", adminService.boardCategoryWrite(boardCategoryWrite));

		return response;
	}

	@ApiOperation(value = "새 게시판 말머리 저장")
	@RequestMapping(value = "/board/category/write", method = RequestMethod.POST)
	public EmptyJsonResponse writeBoardCategory(@RequestBody BoardCategory boardCategory) {

		// spring-data-rest를 사용하자.

		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "대회별 관중수 목록")
	@RequestMapping(value = "/league/attendances", method = RequestMethod.GET)
	public Map<String, Object> getLeagueAttendances(@RequestParam(required = false) String competitionId,
	                                                @RequestParam(required = false) String competitionCode) {

		Competition competition = null;
		List<AttendanceLeague> leagueAttendances;

		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));

		if (Objects.nonNull(competitionId)) {
			competition = competitionService.findOneById(competitionId);
		} else if (Objects.nonNull(competitionCode)) {
			competition = competitionService.findCompetitionByCode(competitionCode);
		}

		if (Objects.isNull(competition)) {
			leagueAttendances = statsService.findLeagueAttendances(sort);
		} else {
			leagueAttendances = statsService.findLeagueAttendances(competition, sort);
		}

		List<Competition> competitions = competitionService.findCompetitions();

		Map<String, Object> response = new HashMap<>();
		response.put("leagueAttendances", leagueAttendances);
		response.put("competitions", competitions);

		return response;
	}

	@ApiOperation(value = "대회별 관중수 하나")
	@RequestMapping(value = "/league/attendance/{id}", method = RequestMethod.GET)
	public Map<String, Object> getLeagueAttendance(@PathVariable String id) {

		AttendanceLeague attendanceLeague = statsService.findOneById(id);

		List<Competition> competitions = competitionService.findCompetitions();

		Map<String, Object> response = new HashMap<>();
		response.put("leagueAttendance", attendanceLeague);
		response.put("competitions", competitions);

		return response;
	}

	@ApiOperation(value = "새 대회별 관중수 하나 저장")
	@RequestMapping(value = "/league/attendance", method = RequestMethod.POST)
	public EmptyJsonResponse addLeagueAttendance(@Valid @RequestBody LeagueAttendanceForm form) {

		Competition competition = competitionService.findOneById(form.getCompetitionId());

		AttendanceLeague attendanceLeague = AttendanceLeague.builder()
				.id(null)
				.competition(competition)
				.season(form.getSeason())
				.games(form.getGames())
				.total(form.getTotal())
				.average(form.getAverage())
				.numberOfClubs(form.getNumberOfClubs())
				.build();

		statsService.saveLeagueAttendance(attendanceLeague);

		return EmptyJsonResponse.newInstance();

	}

	@ApiOperation(value = "대회별 관중수 하나 편집")
	@RequestMapping(value = "/league/attendance/{id}", method = RequestMethod.PUT)
	public EmptyJsonResponse editLeagueAttendance(@PathVariable String id,
												  @Valid @RequestBody LeagueAttendanceForm form) {

		// 존재 확인
		statsService.findOneById(id);

		Competition competition = competitionService.findOneById(form.getCompetitionId());

		AttendanceLeague attendanceLeague = AttendanceLeague.builder()
			.id(id)
			.competition(competition)
			.season(form.getSeason())
			.games(form.getGames())
			.total(form.getTotal())
			.average(form.getAverage())
			.numberOfClubs(form.getNumberOfClubs())
			.build();

		statsService.saveLeagueAttendance(attendanceLeague);

		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "대회별 관중수 하나 지움")
	@RequestMapping(value = "/league/attendance/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteLeagueAttendance(@PathVariable String id) {

		// 존재 확인
		statsService.findOneById(id);

		statsService.deleteLeagueAttendance(id);

		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "클럽 관중수 목록")
	@RequestMapping(value = "/club/attendances", method = RequestMethod.GET)
	public Map<String, Object> getAttendanceClubs() {

		Map<String, Object> data = new HashMap<>();
		data.put("attendanceClubs", adminService.getAttendanceClubList());

		return data;
	}

	@ApiOperation(value = "클럽 관중수 하나")
	@RequestMapping(value = "/club/attendance/{id}", method = RequestMethod.GET)
	public Map<String, Object> getAttendanceClub(@PathVariable String id) {

		AttendanceClub attendanceClub =  statsService.findAttendanceClubById(id);

		AttendanceClubForm attendanceClubForm = new AttendanceClubForm();
		BeanUtils.copyProperties(attendanceClub, attendanceClubForm);

		if (! ObjectUtils.isEmpty(attendanceClub.getClub()))
			attendanceClubForm.setOrigin(attendanceClub.getClub().getId());

		Map<String, Object> response = new HashMap<>();
		response.put("attendanceClubWrite", attendanceClubForm);

		return response;
	}

	@ApiOperation(value = "클럽 관중수 변경")
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

	@ApiOperation(value = "클럽 관중수 추가")
	@RequestMapping(value = "/club/attendance", method = RequestMethod.POST)
	public EmptyJsonResponse editAttendanceClub(@Valid @RequestBody AttendanceClubForm attendanceClubWrite) {

		adminService.saveAttendanceClub(
				null, attendanceClubWrite.getOrigin(), attendanceClubWrite.getLeague(),
				attendanceClubWrite.getSeason(), attendanceClubWrite.getGames(), attendanceClubWrite.getTotal(),
				attendanceClubWrite.getAverage()
		);

		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "게시판 말머리 초기화")
	@RequestMapping(value = "/board/category/init", method = RequestMethod.POST)
	public EmptyJsonResponse initBoardCategory() {
		boardCategoryService.initBoardCategory();

		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "검색 인덱스 초기화")
	@RequestMapping(value = "/search/index/init", method = RequestMethod.POST)
	public EmptyJsonResponse initSearchIndex() {

		searchService.createIndexBoard();
		searchService.createIndexGallery();

		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "검색 데이터 초기화")
	@RequestMapping(value = "/search/data/init", method = RequestMethod.POST)
	public EmptyJsonResponse initSearchData() {
		try {
			searchService.processBulkInsertBoard();
			searchService.processBulkInsertComment();
			searchService.processBulkInsertGallery();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return EmptyJsonResponse.newInstance();
	}

	@RequestMapping(value = "/thumbnail/size", method = RequestMethod.GET)
	public Map<String, Object> thumbnailSizeWrite() {

		Map<String, Object> data = new HashMap<>();
		data.put("resWidth", CoreConst.GALLERY_THUMBNAIL_SIZE_WIDTH);
		data.put("resHeight", CoreConst.GALLERY_THUMBNAIL_SIZE_HEIGHT);

		return data;
	}

	@ApiOperation(value = "썸네일 크기 지정")
	@RequestMapping(value = "/thumbnail/size", method = RequestMethod.POST)
	public EmptyJsonResponse thumbnailSizeWrite(@RequestBody ThumbnailSizeWrite thumbnailSizeWrite) {
		adminService.thumbnailSizeWrite(thumbnailSizeWrite);
		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "경기 목록")
	@RequestMapping(value = "/competitions", method = RequestMethod.GET)
	public Map<String, Object> getCompetitions() {
		Map<String, Object> data = new HashMap<>();
		data.put("competitions", adminService.getCompetitions());
		return data;
	}

	@ApiOperation(value = "경기 하나")
	@RequestMapping(value = "/competition/{id}", method = RequestMethod.GET)
	public CompetitionWrite getCompetition(@PathVariable String id) {
		return adminService.getCompetition(id);
	}

	@ApiOperation(value = "경기 추가")
	@RequestMapping(value = "/competition", method = RequestMethod.POST)
	public CompetitionWrite addCompetition(@RequestBody CompetitionWrite competitionWrite) {
		String id = adminService.writeCompetition(null, competitionWrite).getId();
		return adminService.getCompetition(id);
	}

	@ApiOperation(value = "경기 수정")
	@RequestMapping(value = "/competition/{id}", method = RequestMethod.PUT)
	public EmptyJsonResponse editCompetition(@PathVariable String id, @RequestBody CompetitionWrite competitionWrite) {
		adminService.writeCompetition(id, competitionWrite);
		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "경기 삭제")
	@RequestMapping(value = "/competition/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteCompetition(@PathVariable String id) {
		adminService.deleteCompetition(id);
		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "작두 하나")
	@RequestMapping(value = "/jakdu/schedule/{id}", method = RequestMethod.GET)
	public JakduScheduleWrite addJakduSchedule(@PathVariable String id) {
		return adminService.getJakduScheduleWrite(id);
	}

	@ApiOperation(value = "작두 추가")
	@RequestMapping(value = "/jakdu/schedule", method = RequestMethod.POST)
	public JakduSchedule jakduScheduleWrite(@RequestBody JakduScheduleWrite jakduScheduleWrite) {
		return adminService.writeJakduSchedule(null, jakduScheduleWrite);
	}

	@ApiOperation(value = "작두 수정")
	@RequestMapping(value = "/jakdu/schedule", method = RequestMethod.PUT)
	public EmptyJsonResponse editJakduSchedule(@PathVariable String id, @RequestBody JakduScheduleWrite jakduScheduleWrite) {
		adminService.writeJakduSchedule(id, jakduScheduleWrite);
		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "작두 삭제")
	@RequestMapping(value = "/jakdu/schedule/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteSchedule(@PathVariable String id) {
		adminService.deleteJakduSchedule(id);
		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "작두 목록")
	@RequestMapping(value = "/jakdu/schedules", method = RequestMethod.GET)
	public Map<String, Object> jakduSchedules() {
		Map<String, Object> data = new HashMap<>();
		data.put("jakduSchedules", adminService.getDataJakduScheduleList());
		return data;
	}

	@ApiOperation(value = "작두그룹 하나")
	@RequestMapping(value = "/jakdu/schedule/group/{id}", method = RequestMethod.GET)
	public JakduScheduleGroupWrite getJakduScheduleGroup(@PathVariable String id) {
		return adminService.getJakduScheduleGroupWrite(id);
	}

	@ApiOperation(value = "작두그룹 추가")
	@RequestMapping(value = "/jakdu/schedule/group", method = RequestMethod.POST)
	public JakduScheduleGroup addJakduScheduleGroup(@RequestBody JakduScheduleGroupWrite jakduScheduleGroupWrite) {
		return adminService.writeJakduScheduleGroup(null, jakduScheduleGroupWrite);
	}

	@ApiOperation(value = "작두그룹 수정")
	@RequestMapping(value = "/jakdu/schedule/group", method = RequestMethod.PUT)
	public JakduScheduleGroup editJakduScheduleGroup(@PathVariable String id, @RequestBody JakduScheduleGroupWrite jakduScheduleGroupWrite) {
		return adminService.writeJakduScheduleGroup(id, jakduScheduleGroupWrite);
	}

	@ApiOperation(value = "작두그룹 삭제")
	@RequestMapping(value = "jakdu/schedule/group/{id}", method = RequestMethod.DELETE)
	public EmptyJsonResponse deleteJakduScheduleGroup(@PathVariable String id) {
		adminService.deleteJakduScheduleGroup(id);
		return EmptyJsonResponse.newInstance();
	}

	@ApiOperation(value = "작두그룹 목록")
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

		LocalName footballClubNameKr = LocalName.builder()
			.language(Locale.KOREAN.getLanguage())
			.fullName(request.getFullNameKr())
			.shortName(request.getShortNameKr())
			.build();

		LocalName footballClubNameEn = LocalName.builder()
			.language(Locale.ENGLISH.getLanguage())
			.fullName(request.getFullNameEn())
			.shortName(request.getShortNameEn())
			.build();

		ArrayList<LocalName> names = new ArrayList<>();
		names.add(footballClubNameKr);
		names.add(footballClubNameEn);

		return FootballClub.builder()
			.id(id)
			.active(request.getActive())
			.origin(footballClubOrigin)
			.names(names)
			.build();
	}
}
