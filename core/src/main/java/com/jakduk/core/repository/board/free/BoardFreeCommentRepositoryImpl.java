package com.jakduk.core.repository.board.free;

import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.elasticsearch.ESComment;
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
}
