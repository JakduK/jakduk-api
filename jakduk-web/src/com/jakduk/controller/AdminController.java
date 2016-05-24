package com.jakduk.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.model.web.AttendanceClubWrite;
import com.jakduk.model.web.BoardCategoryWrite;
import com.jakduk.model.web.CompetitionWrite;
import com.jakduk.model.web.ThumbnailSizeWrite;
import com.jakduk.model.web.jakdu.JakduScheduleGroupWrite;
import com.jakduk.model.web.jakdu.JakduScheduleWrite;
import com.jakduk.service.AdminService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping
	public String home() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/board/category/init")
	public String initBoardCategory() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/search/index/init")
	public String initSearchIndex() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/search/type/init")
	public String initSearchType() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/search/data/init")
	public String initSearchData() {
		return "admin/admin";
	}

	@RequestMapping(value = "/encyclopedia/write")
	public String writeEncyclopedia() {
		return "admin/admin";
	}

	@RequestMapping(value = "/encyclopedia/write/{id}")
	public String editEncyclopedia() {
		return "admin/admin";
	}

	@RequestMapping(value = "/encyclopedia")
	public String getEncyclopedia() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fc")
	public String getfc() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fc/write")
	public String writefc() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fc/write/{id}")
	public String editfc() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fcOrigin")
	public String getfcOrigin() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fcOrigin/write")
	public String writefcOrigin() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fcOrigin/write/{id}")
	public String editfcOrigin() {
		return "admin/admin";
	}

	@RequestMapping(value = "/boardCategory")
	public String getBoardCategory() {
		return "admin/admin";
	}

	@RequestMapping(value = "/homeDescription")
	public String getHomeDescription() {
		return "admin/admin";
	}

	@RequestMapping(value = "/homeDescription/write")
	public String writeHomeDescription() {
		return "admin/admin";
	}

	@RequestMapping(value = "/homeDescription/write/{id}")
	public String editHomeDescription() {
		return "admin/admin";
	}

	@RequestMapping(value = "/attendanceLeague", method = RequestMethod.GET)
	public String getAttendanceLeague() {
		return "admin/admin";
	}

	@RequestMapping(value = "/attendanceClub", method = RequestMethod.GET)
	public String getAttendanceClub() {
		return "admin/admin";
	}

	@RequestMapping(value = "/jakduSchedule", method = RequestMethod.GET)
	public String getJakduSchedule() {
		return "admin/admin";
	}

	@RequestMapping(value = "/jakduScheduleGroup", method = RequestMethod.GET)
	public String getJakduScheduleGroup() {
		return "admin/admin";
	}

	@RequestMapping(value = "/competition", method = RequestMethod.GET)
	public String getCompetition() {
		return "admin/admin";
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
			log.debug("result=" + result);
			return "admin/boardCategoryWrite";
		}
		
		adminService.boardCategoryWrite(boardCategoryWtite);

		return "redirect:/admin/boardCategory";
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
			log.debug("result=" + result);
			return "admin/thumbnailSizeWrite";
		}
		
		adminService.thumbnailSizeWrite(thumbnailSizeWrite);

		return "redirect:/admin";
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
			log.debug("result=" + result);
			return "admin/attendanceLeagueWrite";
		}
		
		adminService.attendanceLeagueWrite(attendanceLeague);

		return "redirect:/admin/attendanceLeague";
	}
	
	@RequestMapping(value = "/attendance/league/delete/{id}", method = RequestMethod.GET)
	public String attendanceLeagueDelete(@PathVariable String id) {
		
		boolean result = adminService.attendanceLeagueDelete(id);
		
		return "redirect:/admin/attendanceLeague";
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
			log.debug("result=" + result);
			return "admin/attendanceClubWrite";
		}
		
		adminService.attendanceClubWrite(attendanceClubWrite);

		return "redirect:/admin/attendanceClub";
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
			log.debug("result=" + result);
			return "admin/jakduScheduleWrite";
		}

		adminService.writeJakduSchedule(jakduScheduleWrite);

		return "redirect:/admin/jakduSchedule";
	}

	@RequestMapping(value = "/jakdu/schedule/delete/{id}", method = RequestMethod.GET)
	public String jakduScheduleDelete(@PathVariable String id) {

		boolean result = adminService.deleteJakduSchedule(id);

		return "redirect:/admin/jakduSchedule";
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
			log.debug("result=" + result);
			return "admin/jakduScheduleGroupWrite";
		}

		adminService.writeJakduScheduleGroup(jakduScheduleGroupWrite);

		return "redirect:/admin/jakduScheduleGroup";
	}

	@RequestMapping(value = "jakdu/schedule/group/delete/{id}", method = RequestMethod.GET)
	public String jakduScheduleGroupDelete(@PathVariable String id) {

		boolean result = adminService.deleteJakduScheduleGroup(id);

		return "redirect:/admin/jakduScheduleGroup";
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
			log.debug("result=" + result);
			return "admin/competitionWrite";
		}

		adminService.writeCompetition(competitionWrite);

		return "redirect:/admin/competition";
	}

	@RequestMapping(value = "/data/competition", method = RequestMethod.GET)
	public void dataCompetition(Model model) {

		adminService.getDataCompetitionList(model);
	}

}
