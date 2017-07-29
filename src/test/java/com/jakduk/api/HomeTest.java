package com.jakduk.api;


import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.simple.BoardFreeCommentOnHome;
import com.jakduk.api.model.simple.GallerySimple;
import com.jakduk.api.model.simple.UserOnHome;
import com.jakduk.api.repository.board.free.comment.BoardFreeCommentOnHomeRepository;
import com.jakduk.api.repository.footballclub.FootballClubOriginRepository;
import com.jakduk.api.service.CommonService;
import com.jakduk.api.service.FootballService;
import com.jakduk.api.service.HomeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

public class HomeTest extends ApiApplicationTests {
	
	@Autowired
	BoardFreeCommentOnHomeRepository boardFreeCommentOnHomeRepository;

	@Autowired
	FootballClubOriginRepository footballClubOriginRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	private FootballService footballService;

	@Autowired
	private HomeService homeService;
	
	@Autowired
	JakdukDAO jakdukDAO;

	@Test
	public void getUserLatest() {
		
		List<UserOnHome> users = jakdukDAO.getUserOnHome("ko");
		
		System.out.println("getUsersLatest=" +users);
	}
	
	@Test
	public void getCommentLatest() {
		List<BoardFreeCommentOnHome> comments = homeService.getBoardCommentsLatest();

		System.out.println("getCommentLatest=" +comments);
	}
	
	@Test
	public void getFootballClubList() {

		List<FootballClub> footballClubs = footballService.getFootballClubs("ko", JakdukConst.CLUB_TYPE.FOOTBALL_CLUB, JakdukConst.NAME_TYPE.shortName);

		System.out.println("getFootballClubs=" + footballClubs);
	}

}
