package com.jakduk.api.dao;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.aggregate.SupporterCount;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.model.db.JakduComment;
import com.jakduk.api.model.db.JakduScheduleGroup;
import com.jakduk.api.model.simple.UserOnHome;
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
import java.util.Objects;

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
		AggregationOperation limit = Aggregation.limit(Constants.HOME_SIZE_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(sort, limit);
		
		AggregationResults<UserOnHome> results = mongoTemplate.aggregate(aggregation, "user", UserOnHome.class);
		
		List<UserOnHome> users = results.getMappedResults();

		
		return users;
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
		AggregationOperation limit = Aggregation.limit(Constants.COMMENT_MAX_LIMIT);

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
