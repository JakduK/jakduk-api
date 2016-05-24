package jakduk;

import com.jakduk.common.CommonConst;
import com.jakduk.configuration.AppConfig;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.simple.BoardFreeCommentOnHome;
import com.jakduk.model.simple.UserOnHome;
import com.jakduk.repository.BoardFreeCommentOnHomeRepository;
import com.jakduk.repository.BoardFreeOnHomeRepository;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import com.jakduk.service.HomeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class HomeTest {
	
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
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		//System.out.println("getBoardLatest=" + boardFreeOnHomeRepository.findAll(pageable).getContent());
	}
	
	@Test
	public void getUserLatest() {
		
		List<UserOnHome> users = jakdukDAO.getUserOnHome("ko");
		
		//System.out.println("getUsersLatest=" +users);
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

}
