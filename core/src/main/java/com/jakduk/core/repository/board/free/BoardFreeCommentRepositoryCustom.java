package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.elasticsearch.ESComment;
import com.jakduk.core.model.etc.CommonCount;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface BoardFreeCommentRepositoryCustom {

    /**
     * 기준 BoardFreeComment ID 이상의 BoardFreeComment 목록을 가져온다.
     */
    List<ESComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * 게시물 seq에 해당하는 댓글 수를 가져온다.
     */
    List<CommonCount> findCommentsCountBySeqs(List<Integer> arrSeq);
}
