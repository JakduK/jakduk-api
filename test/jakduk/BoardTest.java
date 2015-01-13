package jakduk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.simple.BoardFreeOnComment;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.repository.BoardFreeCommentRepository;
import com.jakduk.repository.BoardFreeRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class BoardTest {
	
	@Autowired
	BoardFreeRepository boardFreeRepository;
	
	@Autowired
	BoardFreeCommentRepository boardFreeCommentRepository;
	
	@Autowired
	JakdukDAO boardFreeDAO;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Test
	public void test01() throws ParseException {
		BoardFree boardFree = boardFreeRepository.findOneBySeq(11);
		
		System.out.println("date=" + Locale.ENGLISH.getDisplayName());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.ENGLISH);
		SimpleDateFormat sf = (SimpleDateFormat) df;
		String p1 = sf.toPattern();
		String p2 = sf.toLocalizedPattern();
		
		System.out.println("p1=" + p1);
		System.out.println("p2=" + p2);
		
		DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		DateTime dt = new DateTime();
		org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String str = fmt.print(dt);
		System.out.println("str=" + str);
		
		LocalDateTime dateTime1 = LocalDateTime.parse("Thu, 5 Jun 2014 05:10:40 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);
		System.out.println(dateTime1);
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat ff = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.KOREA);
				
		LocalDate ld = LocalDate.now();
		DateTimeFormatter df02 = DateTimeFormatter.ISO_DATE;
		System.out.println("ld=" + ld.format(df02));
		
	
		
		Date date2 = f.parse(ld.format(df02));
		System.out.println("date2=" + date2.getTime());

		
	}
	
	@Test
	public void test02() {
		System.out.println(boardFreeRepository.findOne("5460cfe9e4b06faf36d26efc"));
		
		List<String> usingBoard = new ArrayList<String>();
		usingBoard.add(CommonConst.BOARD_NAME_FREE);
		System.out.println("usingBoard=" + usingBoard);
		
		usingBoard = Collections.emptyList();
		System.out.println("usingBoard=" + usingBoard);
//		usingBoard.add(CommonConst.BOARD_NAME_FREE);
		System.out.println("usingBoard=" + usingBoard);
	}
	
	@Test
	public void test03() {
		
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(1);
//		System.out.println("boardFreeOnComment=" + boardFreeOnComment);
		
		BoardItem boardItem = new BoardItem();
		boardItem.setId(boardFreeOnComment.getId());
		boardItem.setSeq(boardFreeOnComment.getSeq());
		
		Integer page = 1; // temp
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(page - 1, 100, sort);
		
		List<BoardFreeComment> comments = boardFreeCommentRepository.findByBoardItem(boardItem, pageable).getContent();
		System.out.println("test03 comments=" + comments);
	}
	
	@Test
	public void mongoAggregationTest01() {
		
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(21);
		arrTemp.add(22);
		arrTemp.add(23);
		
		Map<String, Integer> map = boardFreeDAO.getBoardFreeCommentCount(arrTemp);
		
		System.out.println("mongoAggregationTest01=" + map);
	}
	
	@Test
	public void mongoAggregationTest02() {
		
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(21);
		arrTemp.add(22);
		arrTemp.add(23);
		
		Map<String, Integer> map = boardFreeDAO.getBoardFreeUsersLikingCount(arrTemp);
		
		System.out.println("mongoAggregationTest02=" + map);
	}
	
	@Test
	public void getNticeList01() {

		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(0, 10, sort);
		
		List<BoardFreeOnList> posts = boardFreeRepository.findByNotice(pageable).getContent();
		
		System.out.println("getNticeList01=" + posts);
	}
}
