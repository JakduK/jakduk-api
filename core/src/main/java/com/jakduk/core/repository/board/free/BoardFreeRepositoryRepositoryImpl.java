package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.simple.BoardFreeSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 9.
 */

@Repository
public class BoardFreeRepositoryRepositoryImpl implements BoardFreeRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<BoardFreeSimple> findByUserId(String userId, Integer limit) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("writer.userId").is(userId));
        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.DESC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);
        Aggregation aggregation = Aggregation.newAggregation(match1, sort1, limit1);
        AggregationResults<BoardFreeSimple> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeSimple.class);

        return results.getMappedResults();
    }
}
