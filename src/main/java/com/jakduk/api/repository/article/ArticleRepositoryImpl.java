package com.jakduk.api.repository.article;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.aggregate.BoardFeelingCount;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.simple.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 10. 9.
 */

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ArticleOnList> findByIdAndUserId(ObjectId id, String userId, Integer limit) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("writer.userId").is(userId));
        AggregationOperation match2 = Aggregation.match(Criteria.where("_id").ne(id));
        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.DESC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);
        Aggregation aggregation = Aggregation.newAggregation(match1, match2, sort1, limit1);
        AggregationResults<ArticleOnList> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE, ArticleOnList.class);

        return results.getMappedResults();
    }

    /**
     * 기준 Article ID 이상의 Article 목록을 가져온다.
     */
    @Override
    public List<Article> findPostsGreaterThanId(ObjectId objectId, Integer limit) {
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

        AggregationResults<Article> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE, Article.class);

        return results.getMappedResults();

    }

    /**
     * RSS 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    @Override
    public List<ArticleOnRSS> findPostsOnRss(ObjectId objectId, Sort sort, Integer limit) {

        Query query = new Query();
        query.addCriteria(Criteria.where("status.delete").ne(true));

        if (Objects.nonNull(objectId))
            query.addCriteria(Criteria.where("_id").lt(objectId));

        query.with(sort);
        query.limit(limit);

        return mongoTemplate.find(query, ArticleOnRSS.class);
    }

    /**
     * id 배열에 해당하는 Article 목록.
     * @param ids id 배열
     */
    @Override
    public List<ArticleOnSearch> findPostsOnSearchByIds(List<ObjectId> ids) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(ids));
        Aggregation aggregation = Aggregation.newAggregation(match1);
        AggregationResults<ArticleOnSearch> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE, ArticleOnSearch.class);

        return results.getMappedResults();
    }

    /**
     * 공지 글 목록
     */
    @Override
    public List<ArticleOnList> findNotices(String board, Sort sort) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status.notice").is(true))
                .addCriteria(Criteria.where("board").is(board))
                .with(sort)
                .limit(10);

        return mongoTemplate.find(query, ArticleOnList.class);
    }

    /**
     * 홈에서 보여지는 최근글 목록
     */
    @Override
    public List<ArticleOnList> findLatest(Sort sort, Integer limit) {
        Query query = new Query();
        query.with(sort);
        query.limit(limit);

        return mongoTemplate.find(query, ArticleOnList.class);
    }

    /**
     * 사이트맵 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    @Override
    public List<ArticleOnSitemap> findPostsOnSitemap(ObjectId objectId, Sort sort, Integer limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status.delete").ne(true));

        if (Objects.nonNull(objectId))
            query.addCriteria(Criteria.where("_id").lt(objectId));

        query.with(sort);
        query.limit(limit);

        return mongoTemplate.find(query, ArticleOnSitemap.class);
    }

    /**
     * 글 보기에서 앞 글, 뒷 글의 정보를 가져온다.
     */
    @Override
    public ArticleSimple findByIdAndCategoryWithOperator(ObjectId id, String category, Constants.CRITERIA_OPERATOR operator) {
        Query query = new Query();

        if (StringUtils.isNotBlank(category))
            query.addCriteria(Criteria.where("category").is(category));

        switch (operator) {
            case GT:
                query.addCriteria(Criteria.where("_id").gt(id));
                query.with(new Sort(Sort.Direction.ASC, "_id"));
                break;
            case LT:
                query.addCriteria(Criteria.where("_id").lt(id));
                query.with(new Sort(Sort.Direction.DESC, "_id"));
                break;
        }

        return mongoTemplate.findOne(query, ArticleSimple.class);
    }

    /**
     * 게시물의 감정 갯수를 가져온다
     *
     * db.getCollection('boardFree').aggregate([{$match:{seq:{$in:[9]}}}, {$project:{_id:1, usersLikingCount:{$size:{'$ifNull':['$usersLiking', []]}}, usersDislikingCount:{$size:{'$ifNull':['$usersDisliking', []]}}}}])
     */
    @Override
    public List<BoardFeelingCount> findUsersFeelingCount(List<ObjectId> ids) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").in(ids));

        AggregationExpression usersLikingCount = ArrayOperators.Size.lengthOfArray(ConditionalOperators.ifNull("usersLiking").then(new ArrayList<>()));
        AggregationExpression usersDislikingCount = ArrayOperators.Size.lengthOfArray(ConditionalOperators.ifNull("usersDisliking").then(new ArrayList<>()));

        AggregationOperation project1 = Aggregation.project("_id")
                .and(usersLikingCount).as("usersLikingCount")
                .and(usersDislikingCount).as("usersDislikingCount");

        Aggregation aggregation = Aggregation.newAggregation(match1, project1);
        AggregationResults<BoardFeelingCount> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE, BoardFeelingCount.class);

        return results.getMappedResults();
    }

    /**
     * 인기있는 게시물 조회
     *
     * db.boardFree.aggregate(
     *  {$match:{_id:{$gt:ObjectId("5947f1b8479fff0441f1b95b")}}},
     *  {$project:{_id:1, seq:1, status:1, subject:1, views:1, count:{$size:{'$ifNull':['$usersLiking', []]}}}},
     *  {$sort:{count:-1, views:-1}},
     *  {$limit:3})
     */
    @Override
    public List<BoardTop> findTopLikes(String board, ObjectId objectId) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").gt(objectId).and("board").is(board));

        AggregationExpression usersLikingCount = ArrayOperators.Size.lengthOfArray(ConditionalOperators.ifNull("usersLiking").then(new ArrayList<>()));

        AggregationOperation project1 = Aggregation.project("_id", "seq", "status", "subject", "views")
                .and(usersLikingCount).as("count");

        AggregationOperation sort1 = Aggregation.sort(Sort.Direction.DESC, "count");
        AggregationOperation sort2 = Aggregation.sort(Sort.Direction.DESC, "views");
        AggregationOperation limit1 = Aggregation.limit(Constants.BOARD_TOP_LIMIT);

        Aggregation aggregation = Aggregation.newAggregation(match1, project1, sort1, sort2, limit1);
        AggregationResults<BoardTop> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE, BoardTop.class);

        return results.getMappedResults();
    }

}
