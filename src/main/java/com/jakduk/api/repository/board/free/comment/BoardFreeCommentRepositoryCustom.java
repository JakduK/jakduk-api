package com.jakduk.api.repository.board.free.comment;

import com.jakduk.api.model.db.BoardFreeComment;
import com.jakduk.api.model.aggregate.CommonCount;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface BoardFreeCommentRepositoryCustom {

    /**
     * 기준 BoardFreeComment ID 이상의 BoardFreeComment 목록을 가져온다.
     */
    List<BoardFreeComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * 게시물 ID 에 해당하는 댓글 수를 가져온다.
     */
    List<CommonCount> findCommentsCountByIds(List<ObjectId> ids);

    /**
     * Board Seq와 기준 BoardFreeComment ID(null 가능) 이상의 BoardFreeComment 목록을 가져온다.
     *
     * @param boardSeq 게시물 seq
     * @param commentId 댓글 ID
     */
    List<BoardFreeComment> findByBoardSeqAndGTId(Integer boardSeq, ObjectId commentId);

    /**
     * boardItem의 objectId 기준 이상의 댓글 수를 가져온다
     *
     * @param boardId 기준이 되는 boardItem의 boardId
     */
    List<CommonCount> findCommentsCountGreaterThanBoardIdAndBoard(ObjectId boardId, String board);

}
