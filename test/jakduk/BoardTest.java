package jakduk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.simple.BoardFreeOnComment;
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
	
	@Test
	public void test01() {
		BoardFree boardFree = boardFreeRepository.findOneBySeq(11);
		
		System.out.println("date=" + Locale.ENGLISH.getDisplayName());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.ENGLISH);
		SimpleDateFormat sf = (SimpleDateFormat) df;
		String p1 = sf.toPattern();
		String p2 = sf.toLocalizedPattern();
		
				
		System.out.println("p1=" + p1);
		System.out.println("p2=" + p2);
		
		DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
		DateTime dt = new DateTime();
		org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM, yyyy");
		String str = fmt.print(dt);
		
		LocalDateTime dateTime1 = LocalDateTime.parse("Thu, 5 Jun 2014 05:10:40 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);
		System.out.println(dateTime1);
		

		
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
		
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(4);
		System.out.println("boardFreeOnComment=" + boardFreeOnComment);
		
		Integer page = 1; // temp
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(page - 1, 100, sort);
		
		List<BoardFreeComment> comments = boardFreeCommentRepository.findByBoardFree(boardFreeOnComment, pageable).getContent();
		System.out.println("comments=" + comments);
	}
	
}
