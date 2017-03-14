package com.jakduk.core.repository.board.free;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.elasticsearch.ESBoard;
import com.jakduk.core.model.simple.BoardFreeOnList;
import com.jakduk.core.model.simple.BoardFreeOnRSS;
import com.jakduk.core.model.simple.BoardFreeOnSearch;
import com.jakduk.core.model.simple.BoardFreeOnSitemap;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 10. 9.
 */

@Repository
public class BoardFreeRepositoryImpl implements BoardFreeRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<BoardFreeOnList> findByIdAndUserId(ObjectId id, String userId, Integer limit) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("writer.userId").is(userId));
        AggregationOperation match2 = Aggregation.match(Criteria.where("_id").ne(id));
        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.DESC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);
        Aggregation aggregation = Aggregation.newAggregation(match1, match2, sort1, limit1);
        AggregationResults<BoardFreeOnList> results = mongoTemplate.aggregate(aggregation, "boardFree", BoardFreeOnList.class);

        return results.getMappedResults();
    }

    /**
     * 기준 BoardFree ID 이상의 BoardFree 목록을 가져온다.
     */
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

    /**
     * RSS 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    @Override
    public List<BoardFreeOnRSS> findPostsOnRss(ObjectId objectId, Sort sort, Integer limit) {

        Query query = new Query();
        query.addCriteria(Criteria.where("status.delete").ne(true));

        if (Objects.nonNull(objectId))
            query.addCriteria(Criteria.where("_id").lt(objectId));

        query.with(sort);
        query.limit(limit);

        return mongoTemplate.find(query, BoardFreeOnRSS.class);
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

    /**
     * 공지 글 목록
     */
    @Override
    public List<BoardFreeOnList> findNotices(Sort sort) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status.notice").is(true));
        query.with(sort);
        query.limit(10);

        return mongoTemplate.find(query, BoardFreeOnList.class);
    }

    /**
     * 홈에서 보여지는 최근글 목록
     */
    @Override
    public List<BoardFreeOnList> findLatest(Sort sort, Integer limit) {
        Query query = new Query();
        query.with(sort);
        query.limit(limit);

        return mongoTemplate.find(query, BoardFreeOnList.class);
    }

    /**
     * 사이트맵 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    @Override
    public List<BoardFreeOnSitemap> findPostsOnSitemap(ObjectId objectId, Sort sort, Integer limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status.delete").ne(true));

        if (Objects.nonNull(objectId))
            query.addCriteria(Criteria.where("_id").lt(objectId));

        query.with(sort);
        query.limit(limit);

        return mongoTemplate.find(query, BoardFreeOnSitemap.class);
    }
}
