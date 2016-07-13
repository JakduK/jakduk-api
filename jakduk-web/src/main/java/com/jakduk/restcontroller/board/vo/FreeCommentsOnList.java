package com.jakduk.restcontroller.board.vo;

import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.embedded.BoardCommentStatus;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.simple.BoardFreeOnSearchComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 13 오후 11:19
 */

@ApiModel(value = "자유게시판 댓글")
@Getter
@Setter
public class FreeCommentsOnList {

    @ApiModelProperty(value = "댓글ID")
    private String id;

    @ApiModelProperty(value = "연동 글")
    private BoardFreeOnSearchComment boardItem;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "좋아요 회원 목록")
    private List<CommonFeelingUser> usersLiking;

    @ApiModelProperty(value = "싫어요 회원 목록")
    private List<CommonFeelingUser> usersDisliking;

    @ApiModelProperty(value = "댓글상태")
    private BoardCommentStatus status;

    public FreeCommentsOnList(BoardFreeComment comment) {
        this.id = comment.getId();
        this.writer = comment.getWriter();
        this.content = comment.getContent();
        this.usersLiking = comment.getUsersLiking();
        this.usersDisliking = comment.getUsersDisliking();
        this.status = comment.getStatus();

        BoardFreeOnSearchComment boardItem = new BoardFreeOnSearchComment(comment.getBoardItem().getId(), comment.getBoardItem().getSeq());
        this.boardItem = boardItem;
    }
}
