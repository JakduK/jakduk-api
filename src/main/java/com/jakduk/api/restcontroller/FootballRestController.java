package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.service.FootballService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 축구단 API
 *
 * @author pyohwan
 * 16. 3. 20 오후 11:26
 */

@RestController
@RequestMapping("/api/football")
public class FootballRestController {

	@Autowired
	private FootballService footballService;

	// 축구단 목록
	@RequestMapping(value = "/clubs", method = RequestMethod.GET)
	public List<FootballClub> getFootballClubs(@RequestParam String lang) {

		String language = JakdukUtils.getLanguageCode(lang);

		return footballService.getFootballClubs(language, Constants.CLUB_TYPE.FOOTBALL_CLUB,
			Constants.NAME_TYPE.fullName);
	}
}
