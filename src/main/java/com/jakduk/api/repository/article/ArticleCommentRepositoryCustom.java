package com.jakduk.api.repository.article;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.model.simple.ArticleCommentSimple;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface ArticleCommentRepositoryCustom {

	/**
	 * 기준 ArticleComment ID 이상의 ArticleComment 목록을 가져온다.
	 */
	List<ArticleComment> findCommentsGreaterThanId(ObjectId objectId, Integer limit);

	/**
	 * 게시물 ID 에 해당하는 댓글 수를 가져온다.
	 */
	List<CommonCount> findCommentsCountByIds(List<ObjectId> ids);

	/**
	 * Board Seq와 기준 ArticleComment ID(null 가능) 이상의 ArticleComment 목록을 가져온다.
	 *
	 * @param articleSeq 게시물 seq
	 * @param commentId 댓글 ID
	 */
	List<ArticleComment> findByBoardSeqAndGTId(String board, Integer articleSeq, ObjectId commentId);

	/**
	 * boardItem의 objectId 기준 이상의 댓글 수를 가져온다
	 *
	 * @param boardId 기준이 되는 boardItem의 boardId
	 */
	List<CommonCount> findCommentsCountGreaterThanBoardIdAndBoard(ObjectId boardId, Constants.BOARD_TYPE board);

	List<ArticleCommentSimple> findSimpleComments();

}
