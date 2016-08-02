package com.jakduk.api;

import com.jakduk.api.util.AbstractSpringTest;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.simple.BoardFreeCommentOnHome;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.model.simple.UserOnHome;
import com.jakduk.core.repository.BoardFreeCommentOnHomeRepository;
import com.jakduk.core.repository.BoardFreeOnHomeRepository;
import com.jakduk.core.repository.FootballClubOriginRepository;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.FootballService;
import com.jakduk.core.service.HomeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

public class HomeTest extends AbstractSpringTest {
	
	@Autowired
    BoardFreeOnHomeRepository boardFreeOnHomeRepository;
	
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
	public void getBoardLatest() {
		
		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("seq"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		System.out.println("getBoardLatest=" + boardFreeOnHomeRepository.findAll(pageable).getContent());
	}
	
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

		List<FootballClub> footballClubs = footballService.getFootballClubs("ko", CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.shortName);

		System.out.println("getFootballClubs=" + footballClubs);
	}

	@Test
	public void 최근사진목록() {
		List<GalleryOnList> galleries = homeService.getGalleriesLatest();

		System.out.println("galleries=" + galleries);
	}

}
