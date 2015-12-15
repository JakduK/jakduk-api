package com.jakduk.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.elasticsearch.CommentOnES;
import com.jakduk.model.elasticsearch.GalleryOnES;
import com.jakduk.model.etc.CommonCount;
import com.jakduk.model.etc.SupporterCount;
import com.jakduk.model.simple.BoardFreeOnGallery;
import com.jakduk.model.simple.BoardFreeOnRSS;
import com.jakduk.model.simple.GalleryOnList;
import com.jakduk.model.simple.UserOnHome;

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
	
	/**
	 * 축구단 목록 정렬해서 가져온다.
	 * @param language
	 * @return
	 */
	public List<FootballClub> getFootballClubList(String language, String sortProperties) {

		Query query = new Query();
		query.addCriteria(Criteria.where("names.language").is(language));
		query.fields().include("active").include("origin").include("names.$");
		query.with(new Sort(Sort.Direction.DESC, "names.fullName"));
		List<FootballClub> footballClubs = mongoTemplate.find(query, FootballClub.class);
		
		switch (sortProperties) {
		case CommonConst.FOOTBALL_CLUB_SORT_PROPERTIES_FULLNAME:
			footballClubs.sort((f1, f2) -> f1.getNames().get(0).getFullName().compareTo(f2.getNames().get(0).getFullName()));			
			break;
		case CommonConst.FOOTBALL_CLUB_SORT_PROPERTIES_SHORTNAME:
			footballClubs.sort((f1, f2) -> f1.getNames().get(0).getShortName().compareTo(f2.getNames().get(0).getShortName()));			
			break;			
		}

		return footballClubs;
	}
	
	public List<FootballClub> getFootballClubList(String language) {

		Query query = new Query();
		query.addCriteria(Criteria.where("names.language").is(language));		
		query.fields().include("active").include("origin").include("names.$");		
		List<FootballClub> footballClubs = mongoTemplate.find(query, FootballClub.class);

		return footballClubs;
	}
	
	public List<GalleryOnList> getGalleryList(Direction direction, Integer size, ObjectId commentId) {
		
		AggregationOperation match1 = Aggregation.match(Criteria.where("status.status").is("use"));
		AggregationOperation match2 = Aggregation.match(Criteria.where("_id").lt(commentId));
		AggregationOperation sort = Aggregation.sort(direction, "_id");
		AggregationOperation limit = Aggregation.limit(size);
		
		Aggregation aggregation;
		if (commentId != null) {
			aggregation = Aggregation.newAggregation(match1, match2, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		}
		
		AggregationResults<GalleryOnList> results = mongoTemplate.aggregate(aggregation, "gallery", GalleryOnList.class);
		
		return results.getMappedResults();
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

	/**
	 * 사진첩 보기의 앞, 뒤 사진을 가져온다.
	 * @param id
	 * @param direction
	 * @return
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
	
	public HashMap<String, Integer> getGalleryUsersLikingCount(List<ObjectId> arrId) {
		
		AggregationOperation unwind = Aggregation.unwind("usersLiking");
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(arrId));
		AggregationOperation match2 = Aggregation.match(Criteria.where("status.status").is("use"));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(unwind, match1, match2, group);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "gallery", CommonCount.class);
		
		List<CommonCount> boardCommentCount = results.getMappedResults();

		HashMap<String, Integer> commentCount = new HashMap<String, Integer>();
		
		for (CommonCount item : boardCommentCount) {
			commentCount.put(item.getId(), item.getCount());
		}
		
		return commentCount;
	}	
	
	public HashMap<String, Integer> getGalleryUsersDislikingCount(List<ObjectId> arrId) {
		
		AggregationOperation unwind = Aggregation.unwind("usersDisliking");
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(arrId));
		AggregationOperation match2 = Aggregation.match(Criteria.where("status.status").is("use"));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(unwind, match1, match2, group);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "gallery", CommonCount.class);
		
		List<CommonCount> boardCommentCount = results.getMappedResults();

		HashMap<String, Integer> commentCount = new HashMap<String, Integer>();
		
		for (CommonCount item : boardCommentCount) {
			commentCount.put(item.getId(), item.getCount());
		}
		
		return commentCount;
	}		
	
	public List<SupporterCount> getSupportFCCount(String language) {
		AggregationOperation match = Aggregation.match(Criteria.where("supportFC").exists(true));
		AggregationOperation group = Aggregation.group("supportFC").count().as("count");
		AggregationOperation project = Aggregation.project("count").and("_id").as("supportFC");
		AggregationOperation sort = Aggregation.sort(Direction.DESC, "count");
		Aggregation aggregation = Aggregation.newAggregation(match, group, project, sort);
		
		AggregationResults<SupporterCount> results = mongoTemplate.aggregate(aggregation, "user", SupporterCount.class);
		
		List<SupporterCount> users = results.getMappedResults();
		
		for (SupporterCount supporterCount : users) {
			supporterCount.getSupportFC().getNames().removeIf(fcName -> !fcName.getLanguage().equals(language));
		}
		
		return users;
	}
	
	public List<BoardFreeOnRSS> getRSS() {
		AggregationOperation match = Aggregation.match(Criteria.where("status.delete").ne(CommonConst.BOARD_HISTORY_TYPE_DELETE));
		AggregationOperation sort = Aggregation.sort(Direction.DESC, "_id");
		AggregationOperation limit = Aggregation.limit(CommonConst.RSS_SIZE_ITEM);
		Aggregation aggregation = Aggregation.newAggregation(match, sort, limit);
		
		AggregationResults<BoardFreeOnRSS> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnRSS.class);
		
		List<BoardFreeOnRSS> posts = results.getMappedResults();
		
		return posts;
	}	
	
	public List<UserOnHome> getUserOnHome(String language) {
		AggregationOperation sort = Aggregation.sort(Direction.DESC, "_id");
		AggregationOperation limit = Aggregation.limit(CommonConst.HOME_SIZE_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(sort, limit);
		
		AggregationResults<UserOnHome> results = mongoTemplate.aggregate(aggregation, "user", UserOnHome.class);
		
		List<UserOnHome> users = results.getMappedResults();
		
		for (UserOnHome user : users) {
			if (user.getSupportFC() != null) {
				user.getSupportFC().getNames().removeIf(fcName -> !fcName.getLanguage().equals(language));
			}
		}
		
		return users;
	}	
	
	public HomeDescription getHomeDescription() {
		
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "priority"));
		HomeDescription homeDescription = mongoTemplate.findOne(query, HomeDescription.class);
		
		return homeDescription;
	}
	
	public List<BoardFreeOnES> getBoardFreeOnES(ObjectId commentId) {
		AggregationOperation match1 = Aggregation.match(Criteria.where("status.delete").ne(CommonConst.BOARD_HISTORY_TYPE_DELETE));
		AggregationOperation match2 = Aggregation.match(Criteria.where("_id").gt(commentId));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		AggregationOperation limit = Aggregation.limit(CommonConst.ELASTICSEARCH_BULK_LIMIT);
		
		Aggregation aggregation;
		
		if (commentId != null) {
			aggregation = Aggregation.newAggregation(match1, match2, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		}
		
		AggregationResults<BoardFreeOnES> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnES.class);
		
		List<BoardFreeOnES> posts = results.getMappedResults();
		
		for (BoardFreeOnES post : posts) {
			post.setSubject(post.getSubject()
					.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
					.replaceAll("\r|\n|&nbsp;",""));

			post.setContent(post.getContent()
					.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
					.replaceAll("\r|\n|&nbsp;",""));
		}
		
		return posts;
	}
	
	public List<CommentOnES> getCommentOnES(ObjectId commentId) {
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").gt(commentId));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		AggregationOperation limit = Aggregation.limit(CommonConst.ELASTICSEARCH_BULK_LIMIT);
		
		Aggregation aggregation;
		
		if (commentId != null) {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(sort, limit);
		}
		
		AggregationResults<CommentOnES> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", CommentOnES.class);
		
		List<CommentOnES> comments = results.getMappedResults();
		
		for (CommentOnES comment : comments) {

			comment.setContent(comment.getContent()
					.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
					.replaceAll("\r|\n|&nbsp;",""));
		}
		
		return comments;
	}
	
	public List<GalleryOnES> getGalleryOnES(ObjectId commentId) {
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").gt(commentId));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		AggregationOperation limit = Aggregation.limit(CommonConst.ELASTICSEARCH_BULK_LIMIT);
		
		Aggregation aggregation;
		
		if (commentId != null) {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(sort, limit);
		}
		
		AggregationResults<GalleryOnES> results = mongoTemplate.aggregate(aggregation, "gallery", GalleryOnES.class);
		
		List<GalleryOnES> galleries = results.getMappedResults();
		
		return galleries;
	}
	
}
