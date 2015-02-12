package com.jakduk.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.Gallery;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 30.
 * @desc     :
 */

@Repository
public class JakdukDAO {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public HashMap<String, Integer> getBoardFreeCommentCount(List<Integer> arrSeq) {
		
		AggregationOperation match = Aggregation.match(Criteria.where("boardItem.seq").in(arrSeq));
		AggregationOperation group = Aggregation.group("boardItem").count().as("count");
		//AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CommonConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(match, group/*, sort, limit*/);
		AggregationResults<BoardFreeCount> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", BoardFreeCount.class);
		
		List<BoardFreeCount> boardCommentCount = results.getMappedResults();

		HashMap<String, Integer> commentCount = new HashMap<String, Integer>();
		
		for (BoardFreeCount item : boardCommentCount) {
			commentCount.put(item.getId(), item.getCount());
		}
		
		return commentCount;
	}
	
	public HashMap<String, Integer> getBoardFreeUsersLikingCount(List<Integer> arrSeq) {
		
		AggregationOperation unwind = Aggregation.unwind("usersLiking");
		AggregationOperation match = Aggregation.match(Criteria.where("seq").in(arrSeq));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		//AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CommonConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(unwind, match, group);
		AggregationResults<BoardFreeCount> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeCount.class);
		
		List<BoardFreeCount> boardCommentCount = results.getMappedResults();

		HashMap<String, Integer> commentCount = new HashMap<String, Integer>();
		
		for (BoardFreeCount item : boardCommentCount) {
			commentCount.put(item.getId(), item.getCount());
		}
		
		return commentCount;
	}
	
	public HashMap<String, Integer> getBoardFreeUsersDislikingCount(List<Integer> arrSeq) {
		
		AggregationOperation unwind = Aggregation.unwind("usersDisliking");
		AggregationOperation match = Aggregation.match(Criteria.where("seq").in(arrSeq));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		//AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CommonConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(unwind, match, group);
		AggregationResults<BoardFreeCount> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeCount.class);
		
		List<BoardFreeCount> boardCommentCount = results.getMappedResults();

		HashMap<String, Integer> commentCount = new HashMap<String, Integer>();
		
		for (BoardFreeCount item : boardCommentCount) {
			commentCount.put(item.getId(), item.getCount());
		}
		
		return commentCount;
	}
	
	/**
	 * 축구단 목록 정렬해서 가져온다. 허나 정렬이 제대로 지원이 안되는 모양인지 결국 이모양이 됐다.
	 * @param language
	 * @return
	 */
	public List<FootballClub> getFootballClubList(String language) {
		
		Sort.Direction sort;
		
		if (language.equals("ko")) {
			sort = Sort.Direction.DESC;
		} else {
			sort = Sort.Direction.ASC;
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where("names.language").is(language));
		query.fields().include("active").include("origin").include("names.$");
		query.with(new Sort(sort, "names.fullName"));
		List<FootballClub> footballClubs = mongoTemplate.find(query, FootballClub.class);
		
		if (language.equals("ko")) {
			Collections.reverse(footballClubs);
		}

		return footballClubs;
	}
	
	public List<GalleryOnHome> getGalleryList(Integer size) {
		
		AggregationOperation match = Aggregation.match(Criteria.where("status.status").is("use"));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		AggregationOperation limit = Aggregation.limit(size);
		Aggregation aggregation = Aggregation.newAggregation(match, sort, limit);
		AggregationResults<GalleryOnHome> results = mongoTemplate.aggregate(aggregation, "gallery", GalleryOnHome.class);
		
		return results.getMappedResults();
	}
	
	public HashMap<String, Integer> getBoardFreeGalleriesCount(List<Integer> arrSeq) {
		AggregationOperation unwind = Aggregation.unwind("galleries");
		AggregationOperation match = Aggregation.match(Criteria.where("seq").in(arrSeq));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		//AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CommonConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(unwind, match, group);
		AggregationResults<BoardFreeCount> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeCount.class);
		
		List<BoardFreeCount> boardFreeCount = results.getMappedResults();

		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		
		for (BoardFreeCount item : boardFreeCount) {
			counts.put(item.getId(), item.getCount());
		}
		
		return counts;
	}
	
	public List<BoardFreeOnGallery> getBoardFreeOnGallery(ObjectId id) {
		AggregationOperation unwind = Aggregation.unwind("galleries");
		AggregationOperation match = Aggregation.match(Criteria.where("galleries._id").is(id));
		Aggregation aggregation = Aggregation.newAggregation(unwind, match);
		AggregationResults<BoardFreeOnGallery> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnGallery.class);
		
		List<BoardFreeOnGallery> posts = results.getMappedResults();
		
		return posts;
	}
	
/*
	public HashMap<String, BoardFreeOnGallery> getBoardFreeOnGallery(List<ObjectId> arrId) {
		
		AggregationOperation match = Aggregation.match(Criteria.where("_id").in(arrId));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<BoardFreeOnGallery> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnGallery.class);
		
		List<BoardFreeOnGallery> posts = results.getMappedResults();
		
		HashMap<String, BoardFreeOnGallery> postsOnGallery = new HashMap<String, BoardFreeOnGallery>();
		
		for (BoardFreeOnGallery post : posts) {
			BoardFreeOnGallery postOnGallery = new BoardFreeOnGallery();
			postOnGallery.setId(post.getId());
			postOnGallery.setSeq(post.getSeq());
			postOnGallery.setSubject(post.getSubject());
			postsOnGallery.put(post.getId(), postOnGallery);
		}
		
		return postsOnGallery;
	}
*/
		
	public Gallery getGalleryById(ObjectId id, Direction direction) {
		Query query = new Query();
		query.addCriteria(Criteria.where("status.status").is("use"));
		
		if (direction.equals(Sort.Direction.ASC)) {
			query.addCriteria(Criteria.where("_id").gt(id));
		} else if (direction.equals(Sort.Direction.DESC)) {
			query.addCriteria(Criteria.where("_id").lt(id));
		}
		
		query.with(new Sort(direction, "_id"));
		Gallery gallery = mongoTemplate.findOne(query, Gallery.class);
		
		return gallery;
	}
	
	public BoardFreeOnFreeView getBoardFreeById(ObjectId id, String categoty, Direction direction) {
		Query query = new Query();
		
		switch (categoty) {
		case CommonConst.BOARD_CATEGORY_DEVELOP:
		case CommonConst.BOARD_CATEGORY_FOOTBALL:
		case CommonConst.BOARD_CATEGORY_FREE:
			query.addCriteria(Criteria.where("categoryName").is(categoty));	
			break;
		}
		
		if (direction.equals(Sort.Direction.ASC)) {
			query.addCriteria(Criteria.where("_id").gt(id));
		} else if (direction.equals(Sort.Direction.DESC)) {
			query.addCriteria(Criteria.where("_id").lt(id));
		}
		
		query.with(new Sort(direction, "_id"));
		BoardFreeOnFreeView post = mongoTemplate.findOne(query, BoardFreeOnFreeView.class);
		
		return post;
	}
	
}
