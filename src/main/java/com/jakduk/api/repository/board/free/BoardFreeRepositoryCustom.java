package com.jakduk.api.repository.board.free;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.model.db.BoardFree;
import com.jakduk.api.model.simple.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 9.
 */
public interface BoardFreeRepositoryCustom {

    List<BoardFreeOnList> findByIdAndUserId(ObjectId id, String userId, Integer limit);

    /**
     * 기준 BoardFree ID 이상의 BoardFree 목록을 가져온다.
     */
    List<BoardFree> findPostsGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * RSS 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    List<BoardFreeOnRSS> findPostsOnRss(ObjectId objectId, Sort sort, Integer limit);

    /**
     * id 배열에 해당하는 BoardFree 목록.
     * @param ids id 배열
     */
    List<BoardFreeOnSearch> findPostsOnSearchByIds(List<ObjectId> ids);

    /**
     * 공지 글 목록
     */
    List<BoardFreeOnList> findNotices(Sort sort);

    /**
     * 홈에서 보여지는 최근글 목록
     */
    List<BoardFreeOnList> findLatest(Sort sort, Integer limit);

    /**
     * 사이트맵 용 게시물 목록
     *
     * @param objectId 해당 ID 이하의 조건 추가 (null 이면 검사 안함)
     * @param sort sort
     * @param limit limit
     */
    List<BoardFreeOnSitemap> findPostsOnSitemap(ObjectId objectId, Sort sort, Integer limit);

    /**
     * 글 보기에서 앞 글, 뒷 글의 정보를 가져온다.
     */
    BoardFreeSimple findByIdAndCategoryWithOperator(ObjectId id, JakdukConst.BOARD_CATEGORY_TYPE category, JakdukConst.CRITERIA_OPERATOR operator);

}