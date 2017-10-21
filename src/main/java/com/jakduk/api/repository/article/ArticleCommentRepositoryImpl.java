package com.jakduk.api.repository.article;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.simple.ArticleCommentSimple;
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
public class ArticleCommentRepositoryImpl implements ArticleCommentRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 기준 ArticleComment ID 이상의 ArticleComment 목록을 가져온다.
     */
    @Override
    public List<ArticleComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit) {

        AggregationOperation match1 = Aggregation.match(Criteria.where("_id").gt(objectId));
        AggregationOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        AggregationOperation limit1 = Aggregation.limit(limit);

        Aggregation aggregation;

        if (! ObjectUtils.isEmpty(objectId)) {
            aggregation = Aggregation.newAggregation(match1, sort, limit1);
        } else {
            aggregation = Aggregation.newAggregation(sort, limit1);
        }

        AggregationResults<ArticleComment> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE_COMMENT, ArticleComment.class);

        return results.getMappedResults();
    }

    /**
     * 게시물 ID 에 해당하는 댓글 수를 가져온다.
     */
    @Override
    public List<CommonCount> findCommentsCountByIds(List<ObjectId> ids) {
        AggregationOperation match = Aggregation.match(Criteria.where("article._id").in(ids));
        AggregationOperation group = Aggregation.group("article").count().as("count");
        //AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
        //AggregationOperation limit = Aggregation.limit(Constants.BOARD_LINE_NUMBER);
        Aggregation aggregation = Aggregation.newAggregation(match, group/*, sort, limit*/);
        AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE_COMMENT, CommonCount.class);

        return results.getMappedResults();
    }

    /**
     * Board Seq와 기준 ArticleComment ID(null 가능) 이상의 ArticleComment 목록을 가져온다.
     *
     * @param articleSeq  게시물 seq
     * @param commentId 댓글 ID
     */
    @Override
    public List<ArticleComment> findByBoardSeqAndGTId(String board, Integer articleSeq, ObjectId commentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("article.seq").is(articleSeq).and("article.board").is(board));

        if (Objects.nonNull(commentId))
            query.addCriteria(Criteria.where("_id").gt(commentId));

        query.with(new Sort(Sort.Direction.ASC, "_id"));
        query.limit(Constants.COMMENT_MAX_LIMIT);

        return mongoTemplate.find(query, ArticleComment.class);
    }

    /**
     * boardItem의 boardId 기준 이상의 댓글 수를 가져온다
     *
     * @param boardId 기준이 되는 boardItem의 boardId
     */
    @Override
    public List<CommonCount> findCommentsCountGreaterThanBoardIdAndBoard(ObjectId boardId, Constants.BOARD_TYPE board) {
        AggregationOperation match1 = Aggregation.match(Criteria.where("article._id").gt(boardId).and("article.board").is(board.name()));
        AggregationOperation group = Aggregation.group("article").count().as("count");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "count");
        //AggregationOperation limit = Aggregation.limit(Constants.BOARD_TOP_LIMIT);
        Aggregation aggregation = Aggregation.newAggregation(match1, group, sort/*, limit*/);
        AggregationResults<CommonCount> results = mongoTemplate.aggregate(aggregation, Constants.COLLECTION_ARTICLE_COMMENT, CommonCount.class);

        return results.getMappedResults();
    }

    @Override
    public List<ArticleCommentSimple> findSimpleComments() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "_id"));
        query.limit(Constants.HOME_SIZE_LINE_NUMBER);

        return mongoTemplate.find(query, ArticleCommentSimple.class);
    }

}
