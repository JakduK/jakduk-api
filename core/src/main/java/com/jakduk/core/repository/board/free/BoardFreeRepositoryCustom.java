package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.elasticsearch.ESBoard;
import com.jakduk.core.model.simple.BoardFreeOnRSS;
import com.jakduk.core.model.simple.BoardFreeOnSearch;
import com.jakduk.core.model.simple.BoardFreeSimple;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 9.
 */
public interface BoardFreeRepositoryCustom {

    List<BoardFreeSimple> findByIdAndUserId(ObjectId id, String userId, Integer limit);

    // 기준 BoardFree ID 이상의 BoardFree 목록을 가져온다.
    List<ESBoard> findPostsGreaterThanId(ObjectId objectId, Integer limit);

    // RSS 용 게시물 목록
    List<BoardFreeOnRSS> findPostsOnRss();

    /**
     * id 배열에 해당하는 BoardFree 목록.
     * @param ids id 배열
     */
    List<BoardFreeOnSearch> findPostsOnSearchByIds(List<ObjectId> ids);
}