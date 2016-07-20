package com.jakduk;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.simple.BoardFreeCommentOnHome;
import com.jakduk.model.simple.GalleryOnList;
import com.jakduk.model.simple.UserOnHome;
import com.jakduk.repository.BoardFreeCommentOnHomeRepository;
import com.jakduk.repository.BoardFreeOnHomeRepository;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import com.jakduk.service.HomeService;
import com.jakduk.util.AbstractSpringTest;
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
