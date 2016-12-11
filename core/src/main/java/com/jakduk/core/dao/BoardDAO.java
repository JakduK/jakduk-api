package com.jakduk.core.dao;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.etc.BoardFeelingCount;
import com.jakduk.core.model.etc.BoardFreeOnBest;
import com.jakduk.core.model.etc.CommonCount;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import org.apache.commons.collections4.IteratorUtils;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 12. 15.
* @desc     :
*/

@Repository
public class BoardDAO {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private Jongo jongo;

	public Map<String, Integer> getBoardFreeCommentCount(List<Integer> arrSeq) {
		
		AggregationOperation match = Aggregation.match(Criteria.where("boardItem.seq").in(arrSeq));
		AggregationOperation group = Aggregation.group("boardItem").count().as("count");
		//AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CoreConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(match, group/*, sort, limit*/);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", CommonCount.class);

		List<CommonCount> numberOfItems = results.getMappedResults();

		Map<String, Integer> commentCount = numberOfItems.stream().collect(Collectors.toMap(CommonCount::getId, CommonCount::getCount));

		return commentCount;
	}

	public Map<String, BoardFeelingCount> getBoardFreeUsersFeelingCount(List<ObjectId> arrId) {
		MongoCollection boardFreeC = jongo.getCollection("boardFree");

		Iterator<BoardFeelingCount> iPosts = boardFreeC.aggregate("{$match:{_id:{$in:#}}}", arrId)
				.and("{$project:{_id:1, seq:1, usersLikingCount:{$size:{'$ifNull':['$usersLiking', []]}}, usersDislikingCount:{$size:{'$ifNull':['$usersDisliking', []]}}}}")
				.as(BoardFeelingCount.class);

		HashMap<String, BoardFeelingCount> feelingCount = new HashMap<String, BoardFeelingCount>();

		while (iPosts.hasNext()) {
			BoardFeelingCount boardFeelingCount = iPosts.next();
			feelingCount.put(boardFeelingCount.getId().toString(), boardFeelingCount);

		}
		
		return feelingCount;
	}
	
	public HashMap<String, Integer> getBoardFreeGalleriesCount(List<Integer> arrSeq) {
		AggregationOperation unwind = Aggregation.unwind("galleries");
		AggregationOperation match = Aggregation.match(Criteria.where("seq").in(arrSeq));
		AggregationOperation group = Aggregation.group("_id").count().as("count");
		//AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CoreConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(unwind, match, group);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "boardFree", CommonCount.class);
		
		List<CommonCount> boardFreeCount = results.getMappedResults();

		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		
		for (CommonCount item : boardFreeCount) {
			counts.put(item.getId(), item.getCount());
		}
		
		return counts;
	}	
	
	public List<BoardFreeComment> getBoardFreeComment(Integer boardSeq, ObjectId commentId) {
		AggregationOperation match1 = Aggregation.match(Criteria.where("boardItem.seq").is(boardSeq));
		AggregationOperation match2 = Aggregation.match(Criteria.where("_id").gt(commentId));
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		AggregationOperation limit = Aggregation.limit(CoreConst.COMMENT_MAX_SIZE);
		
		Aggregation aggregation;
		
		if (commentId != null) {
			aggregation = Aggregation.newAggregation(match1, match2, sort, limit);
		} else {
			aggregation = Aggregation.newAggregation(match1, sort, limit);
		}
		
		AggregationResults<BoardFreeComment> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", BoardFreeComment.class);
		
		List<BoardFreeComment> comments = results.getMappedResults();
		
		return comments;
	}

	public List<BoardFreeOnBest> getBoardFreeCountOfLikeBest(ObjectId commentId) {

		MongoCollection boardFreeC = jongo.getCollection("boardFree");

		Iterator<BoardFreeOnBest> iPosts = boardFreeC.aggregate("{$match:{_id:{$gt:#}}}", commentId)
				.and("{$project:{_id:1, seq:1, status:1, subject:1, views:1, count:{$size:{'$ifNull':['$usersLiking', []]}}}}")
				.and("{$sort:{count:-1, views:-1}}")
				.and("{$limit:#}", CoreConst.BOARD_TOP_LIMIT)
				.as(BoardFreeOnBest.class);

		return IteratorUtils.toList(iPosts);
	}

	
	public List<BoardFreeOnBest> getBoardFreeListOfTop(List<ObjectId> arrId) {
		
		AggregationOperation match = Aggregation.match(Criteria.where("_id").in(arrId));
		Aggregation aggregation = Aggregation.newAggregation(match);
		AggregationResults<BoardFreeOnBest> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnBest.class);
		
		List<BoardFreeOnBest> posts = results.getMappedResults();
		
		return posts;
	}
	
	public HashMap<String, Integer> getBoardFreeCountOfCommentBest(ObjectId commentId) {
		
		AggregationOperation match1 = Aggregation.match(Criteria.where("boardItem._id").gt(commentId));
		AggregationOperation group = Aggregation.group("boardItem").count().as("count");
		AggregationOperation sort = Aggregation.sort(Direction.DESC, "count");
		//AggregationOperation limit = Aggregation.limit(CoreConst.BOARD_TOP_LIMIT);
		Aggregation aggregation = Aggregation.newAggregation(match1, group, sort/*, limit*/);
		AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", CommonCount.class);
		
		List<CommonCount> boardFreeCount = results.getMappedResults();
		
		HashMap<String, Integer> commentCount = new HashMap<String, Integer>();
		
		for (CommonCount item : boardFreeCount) {
			commentCount.put(item.getId(), item.getCount());
		}
		
		return commentCount;
	}	
	
	public Map<String, BoardFreeOnSearchComment> getBoardFreeOnSearchComment(List<ObjectId> arrId) {
		AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(arrId));
		Aggregation aggregation = Aggregation.newAggregation(match1);
		AggregationResults<BoardFreeOnSearchComment> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnSearchComment.class);
		
		List<BoardFreeOnSearchComment> posts = results.getMappedResults();

		return posts.stream()
				.collect(Collectors.toMap(BoardFreeOnSearchComment::getId, Function.identity()));
	}
	
	/**
	 * 글 보기에서 앞 글, 뒷 글의 정보를 가져온다.
	 * @param id 글 ID
	 * @param category 말머리
	 * @param direction 앞 or 뒤
	 * @return 글 객체
	 */
	public BoardFreeOfMinimum getBoardFreeById(ObjectId id, CoreConst.BOARD_CATEGORY_TYPE category, Direction direction) {
		Query query = new Query();

		switch (category) {
			case FREE:
			case FOOTBALL:
				query.addCriteria(Criteria.where("category").is(category));
				break;
		}
		
		if (direction.equals(Sort.Direction.ASC)) {
			query.addCriteria(Criteria.where("_id").gt(id));
		} else if (direction.equals(Sort.Direction.DESC)) {
			query.addCriteria(Criteria.where("_id").lt(id));
		}
		
		query.with(new Sort(direction, "_id"));

		return mongoTemplate.findOne(query, BoardFreeOfMinimum.class);
	}

    /**
     * 해당 언어에 맞는 게시판 말머리를 가져온다.
     * @param code 말머리 code
     * @param language 언어
     * @return 말머리 객체
     */
    public BoardCategory getBoardCategory(String code, String language) {

        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(code));
        query.addCriteria(Criteria.where("names.language").is(language));
        query.fields().include("code").include("names.$");

        return mongoTemplate.findOne(query, BoardCategory.class);
    }

}
