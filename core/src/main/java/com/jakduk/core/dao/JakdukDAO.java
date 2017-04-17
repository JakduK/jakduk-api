package com.jakduk.core.dao;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.Competition;
import com.jakduk.core.model.db.HomeDescription;
import com.jakduk.core.model.db.JakduComment;
import com.jakduk.core.model.db.JakduScheduleGroup;
import com.jakduk.core.model.etc.CommonCount;
import com.jakduk.core.model.etc.SupporterCount;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.model.simple.UserOnHome;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

	// 사진 목록.
	public List<GalleryOnList> findGalleriesById(Direction direction, Integer size, ObjectId galleryId) {
		
		AggregationOperation match1 = Aggregation.match(Criteria.where("status.status").is(CoreConst.GALLERY_STATUS_TYPE.ENABLE.name()));
		AggregationOperation match2 = Aggregation.match(Criteria.where("_id").lt(galleryId));
		AggregationOperation sort = Aggregation.sort(direction, "_id");
		AggregationOperation limit = Aggregation.limit(size);
		
		Aggregation aggregation;
		if (galleryId != null) {
			aggregation = Aggregation.newAggregation(match1, match2, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		}
		
		AggregationResults<GalleryOnList> results = mongoTemplate.aggregate(aggregation, "gallery", GalleryOnList.class);
		
		return results.getMappedResults();
	}

	// 사진의 좋아요 개수 가져오기.
	public Map<String, Integer> findGalleryUsersLikingCount(List<ObjectId> arrId) {
		
		AggregationOperation unwind = Aggregation.unwind("usersLiking");
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(arrId));
		AggregationOperation match2 = Aggregation.match(Criteria.where("status.status").is(CoreConst.GALLERY_STATUS_TYPE.ENABLE.name()));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(unwind, match1, match2, group);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "gallery", CommonCount.class);
		
		List<CommonCount> likingCounts = results.getMappedResults();

		Map<String, Integer> countMap = likingCounts.stream()
				.collect(Collectors.toMap(CommonCount::getId, CommonCount::getCount));

		return countMap;
	}

	// 사진의 싫어요 개수 가져오기.
	public Map<String, Integer> findGalleryUsersDislikingCount(List<ObjectId> arrId) {
		
		AggregationOperation unwind = Aggregation.unwind("usersDisliking");
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(arrId));
		AggregationOperation match2 = Aggregation.match(Criteria.where("status.status").is(CoreConst.GALLERY_STATUS_TYPE.ENABLE.name()));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		Aggregation aggregation = Aggregation.newAggregation(unwind, match1, match2, group);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "gallery", CommonCount.class);
		
		List<CommonCount> diskingCount = results.getMappedResults();

		Map<String, Integer> countMap = diskingCount.stream()
				.collect(Collectors.toMap(CommonCount::getId, CommonCount::getCount));

		return countMap;
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

	public List<UserOnHome> getUserOnHome(String language) {
		AggregationOperation sort = Aggregation.sort(Direction.DESC, "_id");
		AggregationOperation limit = Aggregation.limit(CoreConst.HOME_SIZE_LINE_NUMBER);
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

	// 대회 목록.
	public List<Competition> getCompetitions(List<ObjectId> ids, String language) {

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		query.addCriteria(Criteria.where("names.language").is(language));
		query.fields().include("code").include("names.$");
		//query.with(new Sort(Sort.Direction.DESC, "date"));
		//query.skip(skip);
		//query.limit(size);

		List<Competition> competitions = mongoTemplate.find(query, Competition.class);

		return competitions;
	}

	public JakduScheduleGroup getJakduScheduleGroupOrderBySeq() {

		Query query = new Query();
		query.with(new Sort(Direction.DESC, "seq"));
		query.limit(1);

		JakduScheduleGroup jakduScheduleGroup = mongoTemplate.findOne(query, JakduScheduleGroup.class);

		return jakduScheduleGroup;
	}

	public List<JakduComment> getJakduComments(String jakduScheduleId, ObjectId commentId) {
		AggregationOperation match1 = Aggregation.match(Criteria.where("jakduScheduleId").is(jakduScheduleId));
		AggregationOperation match2 = Aggregation.match(Criteria.where("_id").gt(commentId));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		AggregationOperation limit = Aggregation.limit(CoreConst.COMMENT_MAX_LIMIT);

		Aggregation aggregation;

		if (Objects.nonNull(commentId)) {
			aggregation = Aggregation.newAggregation(match1, match2, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		}

		AggregationResults<JakduComment> results = mongoTemplate.aggregate(aggregation, "jakduComment", JakduComment.class);

		List<JakduComment> comments = results.getMappedResults();

		return comments;
	}
	
}
