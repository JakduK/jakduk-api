package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.elasticsearch.ESComment;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface BoardFreeCommentRepositoryCustom {

    // 기준 BoardFreeComment ID 이상의 BoardFreeComment 목록을 가져온다.
    List<ESComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit);
}
