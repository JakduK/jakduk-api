package com.jakduk.controller;

import javax.validation.Valid;

import com.jakduk.model.db.*;
import com.jakduk.model.web.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakduk.common.CommonConst;
import com.jakduk.service.AdminService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/admin/settings";
	}	
	
	@RequestMapping("/settings")
	public String root(Model model,
			@RequestParam(required = false) String message,
			@RequestParam(required = false) String open) {
		
		model.addAttribute("message", message);
		
		if (open != null) {
			model.addAttribute("open", open);
		}
		
		return "admin/admin";
	}
	
	@RequestMapping(value = "/board/category/init")
	public String initBoardCategory() {

		String message = adminService.initBoardCategory();
		
		return "redirect:/admin/settings?message=" + message;
	}
	
	@RequestMapping(value = "/search/index/init")
	public String initSearchIndex() {

		String message = adminService.initSearchIndex();
		
		return "redirect:/admin/settings?message=" + message;
	}
	
	@RequestMapping(value = "/search/type/init")
	public String initSearchType() {

		String message = adminService.initSearchType();
		
		return "redirect:/admin/settings?message=" + message;
	}
	
	@RequestMapping(value = "/search/data/init")
	public String initSearchData() {

		String message = adminService.initSearchData();
		
		return "redirect:/admin/settings?message=" + message;
	}
	
	@RequestMapping(value = "/encyclopedia/write", method = RequestMethod.GET)
	public String encyclopediaWrite(Model model) {
		model.addAttribute("encyclopedia", new Encyclopedia());
		
		return "admin/encyclopediaWrite";
	}
	
	@RequestMapping(value = "/encyclopedia/write/{seq}", method = RequestMethod.GET)
	public String encyclopediaWrite(@PathVariable int seq, Model model,
			@RequestParam(required = true) String lang) {
		
		adminService.getEncyclopedia(model, seq, lang);
		
		return "admin/encyclopediaWrite";
	}
	
	@RequestMapping(value = "/encyclopedia/write", method = RequestMethod.POST)
	public String encyclopediaWrite(@Valid Encyclopedia encyclopedia, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "encyclopedia/write";
		}
		
		adminService.encyclopediaWrite(encyclopedia);
		
		return "redirect:/admin/settings?open=encyclopedia";
	}

	@RequestMapping(value = "/encyclopedia", method = RequestMethod.GET)
	public void dataEncyclopedia(Model model) {

		adminService.getEncyclopediaList(model);
	}
	
	@RequestMapping(value = "/footballclub/write", method = RequestMethod.GET)
	public String footballClubWrite(Model model) {
		
		adminService.getFootballClub(model);
		
		return "admin/footballClubWrite";
	}
	
	@RequestMapping(value = "/footballclub/write/{id}", method = RequestMethod.GET)
	public String footballClubWrite(@PathVariable String id, Model model) {
		
		adminService.getFootballClub(model, id);
		
		return "admin/footballClubWrite";
	}
	
	@RequestMapping(value = "/footballclub/write", method = RequestMethod.POST)
	public String footballClubWrite(@Valid FootballClubWrite footballClubWrite, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/footballClubWrite";
		}
		
		adminService.writeFootballClub(footballClubWrite);

		return "redirect:/admin/settings?open=fc";
	}

	@RequestMapping(value = "/footballclub", method = RequestMethod.GET)
	public void dataFootballClub(Model model) {

		adminService.getFootballClubList(model);
	}
	
	@RequestMapping(value = "/footballclub/origin/write", method = RequestMethod.GET)
	public String footballClubOriginWrite(Model model) {

		adminService.getFootballClubOrigin(model);
		
		return "admin/footballClubOriginWrite";
	}
	
	@RequestMapping(value = "/footballclub/origin/write/{id}", method = RequestMethod.GET)
	public String footballClubOriginWrite(@PathVariable String id, Model model) {
		
		adminService.getFootballClubOrigin(model, id);
		
		return "admin/footballClubOriginWrite";
	}
	
	@RequestMapping(value = "/footballclub/origin/write", method = RequestMethod.POST)
	public String footballClubOriginWrite(@Valid FootballClubOrigin footballClubOrigin, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/footballClubOriginWrite";
		}
		
		adminService.writeFootballClubOrigin(footballClubOrigin);

		return "redirect:/admin/settings?open=fcOrigin";
	}

	@RequestMapping(value = "/footballclub/origin", method = RequestMethod.GET)
	public void dataFootballClubOrigin(Model model) {

		adminService.dataFootballClubOriginList(model);
	}
	
	@RequestMapping(value = "/board/category/write", method = RequestMethod.GET)
	public String boardCategoryWrite(Model model) {
		model.addAttribute("boardCategoryWrite", new BoardCategoryWrite());
		
		return "admin/boardCategoryWrite";
	}
	
	@RequestMapping(value = "/board/category/write/{id}", method = RequestMethod.GET)
	public String boardCategoryWrite(Model model,
			@PathVariable String id) {
		
		adminService.getBoardCategory(model, id);
		
		return "admin/boardCategoryWrite";
	}
	
	@RequestMapping(value = "/board/category/write", method = RequestMethod.POST)
	public String boardCategoryWrite(@Valid BoardCategoryWrite boardCategoryWtite, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/boardCategoryWrite";
		}
		
		adminService.boardCategoryWrite(boardCategoryWtite);

		return "redirect:/admin/settings?open=boardCategory";
	}

	@RequestMapping(value = "/board/category", method = RequestMethod.GET)
	public void dataBoardCategory(Model model) {

		adminService.getBoardCategoryList(model);
	}
	
	@RequestMapping(value = "/thumbnail/size/write", method = RequestMethod.GET)
	public String thumbnailSizeWrite(Model model) {
		
		model.addAttribute("resWidth", CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH);
		model.addAttribute("resHeight", CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT);
		model.addAttribute("thumbnailSizeWrite", new ThumbnailSizeWrite());

		return "admin/thumbnailSizeWrite";
	}	
	
	@RequestMapping(value = "/thumbnail/size/write", method = RequestMethod.POST)
	public String thumbnailSizeWrite(@Valid ThumbnailSizeWrite thumbnailSizeWrite, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/thumbnailSizeWrite";
		}
		
		adminService.thumbnailSizeWrite(thumbnailSizeWrite);

		return "redirect:/admin/settings";
	}		
	
	@RequestMapping(value = "/home/description/write", method = RequestMethod.GET)
	public String homeDescriptionWrite(Model model) {
		model.addAttribute("homeDescription", new HomeDescription());
		
		return "admin/homeDescriptionWrite";
	}
	
	@RequestMapping(value = "/home/description/write/{id}", method = RequestMethod.GET)
	public String homeDescriptionWrite(@PathVariable String id, Model model) {
		
		adminService.getHomeDescription(model, id);
		
		return "admin/homeDescriptionWrite";
	}		
	
	@RequestMapping(value = "/home/description/write", method = RequestMethod.POST)
	public String homeDescriptionWrite(@Valid HomeDescription homeDescription, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/homeDescriptionWrite";
		}
		
		adminService.homeDescriptionWrite(homeDescription);

		return "redirect:/admin/settings?open=homeDescription";
	}

	@RequestMapping(value = "/data/home/description", method = RequestMethod.GET)
	public void dataHomeDescription(Model model) {

		adminService.getHomeDescriptionList(model);
	}
	
	@RequestMapping(value = "/attendance/league/write", method = RequestMethod.GET)
	public String attendanceLeagueWrite(Model model) {
		model.addAttribute("attendanceLeague", new AttendanceLeague());
		
		return "admin/attendanceLeagueWrite";
	}	
	
	@RequestMapping(value = "/attendance/league/write/{id}", method = RequestMethod.GET)
	public String attendanceLeagueWrite(@PathVariable String id, Model model) {
		
		adminService.getAttendanceLeague(model, id);
		
		return "admin/attendanceLeagueWrite";
	}	
	
	@RequestMapping(value = "/attendance/league/write", method = RequestMethod.POST)
	public String attendanceLeagueWrite(@Valid AttendanceLeague attendanceLeague, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/attendanceLeagueWrite";
		}
		
		adminService.attendanceLeagueWrite(attendanceLeague);

		return "redirect:/admin/settings?open=attendanceLeague";
	}
	
	@RequestMapping(value = "/attendance/league/delete/{id}", method = RequestMethod.GET)
	public String attendanceLeagueDelete(@PathVariable String id) {
		
		boolean result = adminService.attendanceLeagueDelete(id);
		
		return "redirect:/admin/settings?open=attendanceLeague";
	}

	@RequestMapping(value = "/data/attendance/league", method = RequestMethod.GET)
	public void dataAttendanceLeague(Model model,
									 @RequestParam(required = false) String league) {

		adminService.getAttendanceLeagueList(model, league);
	}
	
	@RequestMapping(value = "/attendance/club/write", method = RequestMethod.GET)
	public String attendanceClubWrite(Model model) {
		
		adminService.getAttendanceClubWrite(model);
		
		return "admin/attendanceClubWrite";
	}
	
	@RequestMapping(value = "/attendance/club/write/{id}", method = RequestMethod.GET)
	public String attendanceClubWrite(@PathVariable String id, Model model) {
		
		adminService.getAttendanceClubWrite(model, id);
		
		return "admin/attendanceClubWrite";
	}	
	
	@RequestMapping(value = "/attendance/club/write", method = RequestMethod.POST)
	public String attendanceClubWrite(@Valid AttendanceClubWrite attendanceClubWrite, BindingResult result) {
		
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/attendanceClubWrite";
		}
		
		adminService.attendanceClubWrite(attendanceClubWrite);

		return "redirect:/admin/settings?open=attendanceClub";
	}

	@RequestMapping(value = "/data/attendance/club", method = RequestMethod.GET)
	public void dataAttendanceClub(Model model) {

		adminService.getAttendanceClubList(model);
	}

	@RequestMapping(value = "/jakdu/schedule/write", method = RequestMethod.GET)
	public String jakduScheduleWrite(Model model) {

		adminService.getJakduScheduleWrite(model);

		return "admin/jakduScheduleWrite";
	}

	@RequestMapping(value = "/jakdu/schedule/write/{id}", method = RequestMethod.GET)
	public String jakduScheduleWrite(@PathVariable String id, Model model) {

		adminService.getJakduScheduleWrite(model, id);

		return "admin/jakduScheduleWrite";
	}

	@RequestMapping(value = "/jakdu/schedule/write", method = RequestMethod.POST)
	public String jakduScheduleWrite(@Valid JakduScheduleWrite jakduScheduleWrite, BindingResult result) {
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/jakduScheduleWrite";
		}

		adminService.writeJakduSchedule(jakduScheduleWrite);

		return "redirect:/admin/settings?open=jakduSchedule";
	}

	@RequestMapping(value = "/jakdu/schedule/delete/{id}", method = RequestMethod.GET)
	public String jakduScheduleDelete(@PathVariable String id) {

		boolean result = adminService.deleteJakduSchedule(id);

		return "redirect:/admin/settings?open=jakduSchedule";
	}

	@RequestMapping(value = "/data/jakdu/schedule", method = RequestMethod.GET)
	public void dataJakduSchedule(Model model) {

		adminService.getDataJakduScheduleList(model);
	}

	@RequestMapping(value = "/jakdu/schedule/group/write", method = RequestMethod.GET)
	public String jakduScheduleGroupWrite(Model model) {

		adminService.getJakduScheduleGroupWrite(model);

		return "admin/jakduScheduleGroupWrite";
	}

	@RequestMapping(value = "/jakdu/schedule/group/write/{id}", method = RequestMethod.GET)
	public String jakduScheduleGroupWrite(@PathVariable String id, Model model) {

		adminService.getJakduScheduleGroupWrite(model, id);

		return "admin/jakduScheduleGroupWrite";
	}

	@RequestMapping(value = "/jakdu/schedule/group/write", method = RequestMethod.POST)
	public String jakduScheduleGroupWrite(@Valid JakduScheduleGroupWrite jakduScheduleGroupWrite, BindingResult result) {
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/jakduScheduleGroupWrite";
		}

		adminService.writeJakduScheduleGroup(jakduScheduleGroupWrite);

		return "redirect:/admin/settings?open=jakduScheduleGroup";
	}

	@RequestMapping(value = "jakdu/schedule/group/delete/{id}", method = RequestMethod.GET)
	public String jakduScheduleGroupDelete(@PathVariable String id) {

		boolean result = adminService.deleteJakduScheduleGroup(id);

		return "redirect:/admin/settings?open=jakduScheduleGroup";
	}

	@RequestMapping(value = "/data/jakdu/schedule/group", method = RequestMethod.GET)
	public void dataJakduScheduleGroup(Model model) {

		adminService.getDataJakduScheduleGroupList(model);
	}

	@RequestMapping(value = "/competition/write", method = RequestMethod.GET)
	public String competitionWrite(Model model) {

		adminService.getCompetition(model);

		return "admin/competitionWrite";
	}

	@RequestMapping(value = "/competition/write/{id}", method = RequestMethod.GET)
	public String competitionWrite(@PathVariable String id, Model model) {

		adminService.getCompetition(model, id);

		return "admin/competitionWrite";
	}

	@RequestMapping(value = "/competition/write", method = RequestMethod.POST)
	public String competitionWrite(@Valid CompetitionWrite competitionWrite, BindingResult result) {
		if (result.hasErrors()) {
			logger.debug("result=" + result);
			return "admin/competitionWrite";
		}

		adminService.writeCompetition(competitionWrite);

		return "redirect:/admin/settings?open=competition";
	}

	@RequestMapping(value = "/data/competition", method = RequestMethod.GET)
	public void dataCompetition(Model model) {

		adminService.getDataCompetitionList(model);
	}

}
