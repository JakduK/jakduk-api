package com.jakduk.api.controller;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.web.AttendanceClubWrite;
import com.jakduk.core.model.web.CompetitionWrite;
import com.jakduk.core.model.web.ThumbnailSizeWrite;
import com.jakduk.core.model.web.jakdu.JakduScheduleGroupWrite;
import com.jakduk.core.model.web.jakdu.JakduScheduleWrite;
import com.jakduk.core.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping
	public String home() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/board/category/init", method = RequestMethod.GET)
	public String initBoardCategory() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/search/index/init", method = RequestMethod.GET)
	public String initSearchIndex() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/search/type/init", method = RequestMethod.GET)
	public String initSearchType() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/search/data/init", method = RequestMethod.GET)
	public String initSearchData() {
		return "admin/admin";
	}

	@RequestMapping(value = "/encyclopedia/write", method = RequestMethod.GET)
	public String writeEncyclopedia() {
		return "admin/admin";
	}

	@RequestMapping(value = "/encyclopedia/write/{id}", method = RequestMethod.GET)
	public String editEncyclopedia() {
		return "admin/admin";
	}

	@RequestMapping(value = "/encyclopedia", method = RequestMethod.GET)
	public String getEncyclopedia() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fc", method = RequestMethod.GET)
	public String getfc() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fc/write", method = RequestMethod.GET)
	public String writefc() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fc/write/{id}", method = RequestMethod.GET)
	public String editfc() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fcOrigin", method = RequestMethod.GET)
	public String getfcOrigin() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fcOrigin/write", method = RequestMethod.GET)
	public String writefcOrigin() {
		return "admin/admin";
	}

	@RequestMapping(value = "/fcOrigin/write/{id}", method = RequestMethod.GET)
	public String editfcOrigin() {
		return "admin/admin";
	}

	@RequestMapping(value = "/boardCategory", method = RequestMethod.GET)
	public String getBoardCategory() {
		return "admin/admin";
	}

	@RequestMapping(value = "/homeDescription", method = RequestMethod.GET)
	public String getHomeDescription() {
		return "admin/admin";
	}

	@RequestMapping(value = "/homeDescription/write", method = RequestMethod.GET)
	public String writeHomeDescription() {
		return "admin/admin";
	}

	@RequestMapping(value = "/homeDescription/write/{id}", method = RequestMethod.GET)
	public String editHomeDescription() {
		return "admin/admin";
	}

	@RequestMapping(value = "/attendanceLeague", method = RequestMethod.GET)
	public String getAttendanceLeague() {
		return "admin/admin";
	}

	@RequestMapping(value = "/attendanceLeague/write", method = RequestMethod.GET)
	public String writeAttendanceLeague() {
		return "admin/admin";
	}

	@RequestMapping(value = "/attendanceLeague/write/{id}", method = RequestMethod.GET)
	public String editAttendanceLeague() {
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

	@RequestMapping(value = "/board/category", method = RequestMethod.GET)
	public String getBoardCategories() {
		return "admin/admin";
	}

	@RequestMapping(value = "/boardCategory/write", method = RequestMethod.GET)
	public String writeBoardCategory() {
		return "admin/admin";
	}
	
	@RequestMapping(value = "/boardCategory/write/{id}", method = RequestMethod.GET)
	public String editBoardCategory() {
		return "admin/admin";
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
