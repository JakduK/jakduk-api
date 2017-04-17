package com.jakduk.api;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.simple.BoardFreeCommentOnHome;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.model.simple.UserOnHome;
import com.jakduk.core.repository.board.free.comment.BoardFreeCommentOnHomeRepository;
import com.jakduk.core.repository.footballclub.FootballClubOriginRepository;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.FootballService;
import com.jakduk.core.service.HomeService;
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

		List<FootballClub> footballClubs = footballService.getFootballClubs("ko", CoreConst.CLUB_TYPE.FOOTBALL_CLUB, CoreConst.NAME_TYPE.shortName);

		System.out.println("getFootballClubs=" + footballClubs);
	}

	@Test
	public void 최근사진목록() {
		List<GalleryOnList> galleries = homeService.getGalleriesLatest();

		System.out.println("galleries=" + galleries);
	}

}
