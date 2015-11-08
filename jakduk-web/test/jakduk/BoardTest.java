package jakduk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardFeelingCount;
import com.jakduk.dao.BoardFreeOnBest;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
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
	JakdukDAO jakdukDAO;
	
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
		
		/*
		DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		DateTime dt = new DateTime();
		org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String str = fmt.print(dt);
		System.out.println("str=" + str);
		*/
		
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
	public void mongoAggregationTest01() {
		
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(21);
		arrTemp.add(22);
		arrTemp.add(23);
		
		Map<String, Integer> map = jakdukDAO.getBoardFreeCommentCount(arrTemp);
		
		System.out.println("mongoAggregationTest01=" + map);
	}
	
	@Test
	public void mongoAggregationTest02() {
		
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(21);
		arrTemp.add(22);
		arrTemp.add(23);
		
		//Map<String, Integer> map = jakdukDAO.getBoardFreeUsersLikingCount(arrTemp);
		List<BoardFeelingCount> map = jakdukDAO.getBoardFreeUsersFeelingCount(arrTemp);
		
		System.out.println("mongoAggregationTest02=" + map);
	}
	
	@Test
	public void getNticeList01() {

		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(0, 10, sort);
		
		List<BoardFreeOnList> posts = boardFreeRepository.findByNotice(pageable).getContent();
		
		System.out.println("getNticeList01=" + posts);
	}
	
	@Test
	public void getGalleriesCount01() {
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(28);
		arrTemp.add(29);
		arrTemp.add(30);
		
		HashMap<String, Integer> galleriesCount = jakdukDAO.getBoardFreeGalleriesCount(arrTemp);
		System.out.println("getGalleriesCount01=" + galleriesCount);
	}
	
	@Test
	public void getFreeComment() {
		
		Integer boardSeq = 13;
		ObjectId commentId = new ObjectId("54b916d73d965cb1dbdd4af6");
		
		List<BoardFreeComment> comments = jakdukDAO.getBoardFreeComment(boardSeq, null);
		
		System.out.println("getFreeComment=" + comments);
		
	}
	
	@Test
	public void isNumeric() {
		
		String val01 = "10";
		String val02 = "football";
		String val03 = "football1";
		String val04 = "1football";
		String val05 = "1football2";
		
		Pattern pattern = Pattern.compile("[+-]?\\d+");
		
		System.out.println("isNumeric=" + pattern.matcher(val01).matches());
		System.out.println("isNumeric=" + pattern.matcher(val02).matches());
		System.out.println("isNumeric=" + pattern.matcher(val03).matches());
		System.out.println("isNumeric=" + pattern.matcher(val04).matches());
		System.out.println("isNumeric=" + pattern.matcher(val05).matches());
	}
	
	@Test
	public void newDate01() {
		
		LocalDate date = LocalDate.now().minusWeeks(1);
		
		System.out.println("date=" + date.format(DateTimeFormatter.BASIC_ISO_DATE));
		
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		
		System.out.println("objectId=" + new ObjectId(Date.from(instant)));
		
		HashMap<String, Integer> boardFreeCount = jakdukDAO.getBoardFreeCountOfLikeBest(new ObjectId(Date.from(instant)));
		
		System.out.println("boardFreeCount=" + boardFreeCount);
		
		ArrayList<ObjectId> ids = new ArrayList<ObjectId>();

		Iterator<?> iterator = boardFreeCount.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
			ObjectId objId = new ObjectId(entry.getKey().toString());
			ids.add(objId);
		}
		
		List<BoardFreeOnBest> boardFreeList = jakdukDAO.getBoardFreeListOfTop(ids);
		
		for (BoardFreeOnBest boardFree : boardFreeList) {
			String id = boardFree.getId();
			Integer count = boardFreeCount.get(id);
			boardFree.setCount(count);
		}
		
		boardFreeList = boardFreeList.stream().sorted((h1, h2) -> h2.getCount() - h1.getCount())
				.collect(Collectors.toList());
		
		System.out.println("boardFreeList=" + boardFreeList);
	}	
	
	@Test
	public void newDate02() {
		
		LocalDate date = LocalDate.now().minusWeeks(1);
		
		System.out.println("date=" + date.format(DateTimeFormatter.BASIC_ISO_DATE));
		
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		
		System.out.println("objectId=" + new ObjectId(Date.from(instant)));
		
		HashMap<String, Integer> boardFreeCount = jakdukDAO.getBoardFreeCountOfCommentBest(new ObjectId(Date.from(instant)));
		
		System.out.println("boardFreeCount2=" + boardFreeCount);
		
		ArrayList<ObjectId> ids = new ArrayList<ObjectId>();

		Iterator<?> iterator = boardFreeCount.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
			ObjectId objId = new ObjectId(entry.getKey().toString());
			ids.add(objId);
		}
		
		List<BoardFreeOnBest> boardFreeList = jakdukDAO.getBoardFreeListOfTop(ids);
		
		for (BoardFreeOnBest boardFree : boardFreeList) {
			String id = boardFree.getId();
			Integer count = boardFreeCount.get(id);
			boardFree.setCount(count);
		}
		
		boardFreeList = boardFreeList.stream().sorted((h1, h2) -> h2.getCount() - h1.getCount())
				.collect(Collectors.toList());
		
		System.out.println("boardFreeList2=" + boardFreeList);
	}		
}
