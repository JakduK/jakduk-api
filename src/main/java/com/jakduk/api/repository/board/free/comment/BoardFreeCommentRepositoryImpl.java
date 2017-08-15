package com.jakduk.api.repository.board.free.comment;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.BoardFreeComment;
import com.jakduk.api.model.aggregate.CommonCount;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 11. 30.
 */

@Repository
public class BoardFreeCommentRepositoryImpl implements BoardFreeCommentRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 기준 BoardFreeComment ID 이상의 BoardFreeComment 목록을 가져온다.
     */
    @Override
    public List<BoardFreeComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit) {

        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").gt(objectId));
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);

        Aggregation aggregation;

        if (! ObjectUtils.isEmpty(objectId)) {
            aggregation = Aggregation.newAggregation(match1, sort, limit1);
        } else {
            aggregation = Aggregation.newAggregation(sort, limit1);
        }

        AggregationResults<BoardFreeComment> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", BoardFreeComment.class);

        return results.getMappedResults();
    }

    /**
     * 게시물 ID 에 해당하는 댓글 수를 가져온다.
     */
    @Override
    public List<CommonCount> findCommentsCountByIds(List<ObjectId> ids) {
        AggregationOperation match = Aggregation.match(Criteria.where("boardItem._id").in(ids));
        AggregationOperation group = Aggregation.group("boardItem").count().as("count");
        //AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
        //AggregationOperation limit = Aggregation.limit(Constants.BOARD_LINE_NUMBER);
        Aggregation aggregation = Aggregation.newAggregation(match, group/*, sort, limit*/);
        AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", CommonCount.class);

        return results.getMappedResults();
    }

    /**
     * Board Seq와 기준 BoardFreeComment ID(null 가능) 이상의 BoardFreeComment 목록을 가져온다.
     *
     * @param boardSeq  게시물 seq
     * @param commentId 댓글 ID
     */
    @Override
    public List<BoardFreeComment> findByBoardSeqAndGTId(Integer boardSeq, ObjectId commentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("boardItem.seq").is(boardSeq));

        if (Objects.nonNull(commentId))
            query.addCriteria(Criteria.where("_id").gt(commentId));

        query.with(new Sort(Sort.Direction.ASC, "_id"));
        query.limit(Constants.COMMENT_MAX_LIMIT);

        return mongoTemplate.find(query, BoardFreeComment.class);
    }

}
