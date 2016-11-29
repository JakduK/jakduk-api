package com.jakduk.core.repository.board.free;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.SearchUtils;
import com.jakduk.core.model.elasticsearch.ESBoardFree;
import com.jakduk.core.model.simple.BoardFreeSimple;
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
 * Created by pyohwan on 16. 10. 9.
 */

@Repository
public class BoardFreeRepositoryImpl implements BoardFreeRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<BoardFreeSimple> findByIdAndUserId(ObjectId id, String userId, Integer limit) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("writer.userId").is(userId));
        AggregationOperation match2 = Aggregation.match(Criteria.where("_id").ne(id));
        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.DESC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);
        Aggregation aggregation = Aggregation.newAggregation(match1, match2, sort1, limit1);
        AggregationResults<BoardFreeSimple> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeSimple.class);

        return results.getMappedResults();
    }

    // 기준 BoardFree ID 이상의 BoardFree 목록을 가져온다.
    @Override
    public List<ESBoardFree> findPostsGreaterThanId(ObjectId objectId, Integer limit) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("status.delete").ne(CoreConst.BOARD_HISTORY_TYPE.DELETE.name()));
        AggregationOperation match2 = Aggregation.match(Criteria.where("_id").gt(objectId));
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);

        Aggregation aggregation;

        if (! ObjectUtils.isEmpty(objectId)) {
            aggregation = Aggregation.newAggregation(match1, match2, sort, limit1);
        } else {
            aggregation = Aggregation.newAggregation(match1, sort, limit1);
        }

        AggregationResults<ESBoardFree> results = mongoTemplate.aggregate(aggregation, "boardFree", ESBoardFree.class);

        List<ESBoardFree> posts = results.getMappedResults();

        posts.forEach(post -> {
            post.setSubject(SearchUtils.stripHtmlTag(post.getSubject()));
            post.setContent(SearchUtils.stripHtmlTag(post.getContent()));
        });

        return posts;
    }
}
