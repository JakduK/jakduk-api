package jakduk;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.UserOnHome;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.simple.BoardFreeCommentOnHome;
import com.jakduk.repository.BoardFreeCommentOnHomeRepository;
import com.jakduk.repository.BoardFreeOnHomeRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class HomeTest {
	
	@Autowired
	BoardFreeOnHomeRepository boardFreeOnHomeRepository;
	
	@Autowired
	BoardFreeCommentOnHomeRepository boardFreeCommentOnHomeRepository;
	
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
		
		//System.out.println("getUserLatest=" +users);
	}
	
	@Test
	public void getCommentLatest() {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		List<BoardFreeCommentOnHome> comments = boardFreeCommentOnHomeRepository.findAll(pageable).getContent();
		
		//System.out.println("getCommentLatest=" +comments);
	}
	
	@Test
	public void getFootballClubList() {
		
		List<FootballClub> footballClubs = jakdukDAO.getFootballClubList("ko", CommonConst.FOOTBALL_CLUB_SORT_PROPERTIES_SHORTNAME);
		
		System.out.println("getFootballClubList=" + footballClubs);
	}

}
