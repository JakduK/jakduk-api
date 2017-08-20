package com.jakduk.api.repository.article;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.aggregate.BoardFeelingCount;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.simple.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 9.
 */
public interface ArticleRepositoryCustom {

    List<ArticleOnList> findByIdAndUserId(ObjectId id, String userId, Integer limit);

    /**
     * 기준 Article ID 이상의 Article 목록을 가져온다.
     */
    List<Article> findPostsGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * RSS 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    List<ArticleOnRSS> findPostsOnRss(ObjectId objectId, Sort sort, Integer limit);

    /**
     * id 배열에 해당하는 Article 목록.
     * @param ids id 배열
     */
    List<ArticleOnSearch> findPostsOnSearchByIds(List<ObjectId> ids);

    /**
     * 공지 글 목록
     */
    List<ArticleOnList> findNotices(String board, Sort sort);

    /**
     * 홈에서 보여지는 최근글 목록
     */
    List<ArticleOnList> findLatest(Sort sort, Integer limit);

    /**
     * 사이트맵 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    List<ArticleOnSitemap> findSitemapArticles(ObjectId objectId, Sort sort, Integer limit);

    /**
     * 글 보기에서 앞 글, 뒷 글의 정보를 가져온다.
     */
    ArticleSimple findByIdAndCategoryWithOperator(ObjectId id, String category, Constants.CRITERIA_OPERATOR operator);

    /**
     * 게시물의 감정 갯수를 가져온다
     */
    List<BoardFeelingCount> findUsersFeelingCount(List<ObjectId> ids);

    /**
     * 인기있는 게시물 조회
     *
     * @param board 게시판
     * @param commentId 해당 ID 기준 이상
     */
    List<BoardTop> findTopLikes(String board, ObjectId commentId);

}