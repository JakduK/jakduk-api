package com.jakduk.api;


import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.dao.BoardDAO;
import com.jakduk.api.model.jongo.BoardFreeOnBest;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

public class BoardTest extends ApiApplicationTests {
	
	@Autowired
    private BoardDAO boardDAO;

	@Autowired
	private Jongo jongo;

	@Test
	public void getBoardFreeCountOfLikeBest01() {
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		List<BoardFreeOnBest> posts = boardDAO.getBoardFreeCountOfLikeBest(new ObjectId(Date.from(instant)));

		System.out.println("getBoardFreeCountOfLikeBest01=" + posts);
	}

	@Test
	public void getGalleriesCount01() {
		ArrayList<Integer> arrTemp = new ArrayList<Integer>();
		arrTemp.add(28);
		arrTemp.add(29);
		arrTemp.add(30);
		
		HashMap<String, Integer> galleriesCount = boardDAO.getBoardFreeGalleriesCount(arrTemp);
		System.out.println("getGalleriesCount01=" + galleriesCount);
	}

	@Test
	public void getBoardFreeCountOfCommentBest01() {
		
		LocalDate date = LocalDate.now().minusWeeks(1);
		
		System.out.println("date=" + date.format(DateTimeFormatter.BASIC_ISO_DATE));
		
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		
		System.out.println("objectId=" + new ObjectId(Date.from(instant)));
		
		HashMap<String, Integer> boardFreeCount = boardDAO.getBoardFreeCountOfCommentBest(new ObjectId(Date.from(instant)));
		
		System.out.println("boardFreeCount2=" + boardFreeCount);
		
		ArrayList<ObjectId> ids = new ArrayList<ObjectId>();

		Iterator<?> iterator = boardFreeCount.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
			ObjectId objId = new ObjectId(entry.getKey().toString());
			ids.add(objId);
		}
		
		List<BoardFreeOnBest> boardFreeList = boardDAO.getBoardFreeListOfTop(ids);
		
		for (BoardFreeOnBest boardFree : boardFreeList) {
			String id = boardFree.getId().toString();
			Integer count = boardFreeCount.get(id);
			boardFree.setCount(count);
		}

		Comparator<BoardFreeOnBest> byCount = (b1, b2) -> b2.getCount() - b1.getCount();
		Comparator<BoardFreeOnBest> byView = (b1, b2) -> b2.getViews() - b1.getViews();

		boardFreeList = boardFreeList.stream()
				.sorted(byCount.thenComparing(byView))
				.limit(JakdukConst.BOARD_TOP_LIMIT)
				.collect(Collectors.toList());
		
		System.out.println("getBoardFreeCountOfCommentBest01=" + boardFreeList);
	}		

	@Test
	public void jongo01() {
		//DB db = mongoClient.getDB("jakduk_test");

		//Jongo jongo = new Jongo(db);
		MongoCollection boardFreeC = jongo.getCollection("boardFree");

		System.out.println(boardFreeC);
		//Map boardFree = boardFreeC.findOne("{seq:1}").as(Map.class);

		Iterator<Map> boardFree = boardFreeC.aggregate("{$project:{_id:1, usersLikingCount:{$size:{'$ifNull':['$usersLiking', []]}}, usersDislikingCount:{$size:{'$ifNull':['$usersDisliking', []]}}}}")
				.and("{$limit:#}", JakdukConst.BOARD_TOP_LIMIT)
                .as(Map.class);

		while (boardFree.hasNext()) {
            System.out.println(boardFree.next());
        }
	}

}
