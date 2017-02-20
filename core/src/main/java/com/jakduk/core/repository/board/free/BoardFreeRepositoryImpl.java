package com.jakduk.core.repository.board.free;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.elasticsearch.ESBoard;
import com.jakduk.core.model.simple.BoardFreeOnRSS;
import com.jakduk.core.model.simple.BoardFreeOnSearch;
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
    public List<ESBoard> findPostsGreaterThanId(ObjectId objectId, Integer limit) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("status.delete").ne(true));
        AggregationOperation match2 = Aggregation.match(Criteria.where("_id").gt(objectId));
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);

        Aggregation aggregation;

        if (! ObjectUtils.isEmpty(objectId)) {
            aggregation = Aggregation.newAggregation(match1, match2, sort, limit1);
        } else {
            aggregation = Aggregation.newAggregation(match1, sort, limit1);
        }

        AggregationResults<ESBoard> results = mongoTemplate.aggregate(aggregation, "boardFree", ESBoard.class);

        List<ESBoard> posts = results.getMappedResults();

        posts.forEach(post -> {
            post.setSubject(CoreUtils.stripHtmlTag(post.getSubject()));
            post.setContent(CoreUtils.stripHtmlTag(post.getContent()));
        });

        return posts;
    }

    @Override
    public List<BoardFreeOnRSS> findPostsOnRss() {
        AggregationOperation match = Aggregation.match(Criteria.where("status.delete").ne(true));
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "_id");
        AggregationOperation limit = Aggregation.limit(CoreConst.RSS_SIZE_ITEM);
        Aggregation aggregation = Aggregation.newAggregation(match, sort, limit);

        AggregationResults<BoardFreeOnRSS> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnRSS.class);

        return results.getMappedResults();
    }

    /**
     * id 배열에 해당하는 BoardFree 목록.
     * @param ids id 배열
     */
    @Override
    public List<BoardFreeOnSearch> findPostsOnSearchByIds(List<ObjectId> ids) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(ids));
        Aggregation aggregation = Aggregation.newAggregation(match1);
        AggregationResults<BoardFreeOnSearch> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnSearch.class);

        return results.getMappedResults();
    }
}
