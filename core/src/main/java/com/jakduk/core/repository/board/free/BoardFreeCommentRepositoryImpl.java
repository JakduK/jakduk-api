package com.jakduk.core.repository.board.free;

import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.elasticsearch.ESComment;
import com.jakduk.core.model.etc.CommonCount;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

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
    public List<ESComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit) {

        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").gt(objectId));
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);

        Aggregation aggregation;

        if (! ObjectUtils.isEmpty(objectId)) {
            aggregation = Aggregation.newAggregation(match1, sort, limit1);
        } else {
            aggregation = Aggregation.newAggregation(sort, limit1);
        }

        AggregationResults<ESComment> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", ESComment.class);

        List<ESComment> comments = results.getMappedResults();

        comments.forEach(comment -> {
            comment.setContent(CoreUtils.stripHtmlTag(comment.getContent()));
        });

        return comments;
    }

    /**
     * 게시물 ID 에 해당하는 댓글 수를 가져온다.
     */
    @Override
    public List<CommonCount> findCommentsCountByIds(List<ObjectId> ids) {
        AggregationOperation match = Aggregation.match(Criteria.where("boardItem._id").in(ids));
        AggregationOperation group = Aggregation.group("boardItem").count().as("count");
        //AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
        //AggregationOperation limit = Aggregation.limit(CoreConst.BOARD_LINE_NUMBER);
        Aggregation aggregation = Aggregation.newAggregation(match, group/*, sort, limit*/);
        AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, "boardFreeComment", CommonCount.class);

        return results.getMappedResults();
    }
}
