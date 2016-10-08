package com.jakduk.api.restcontroller.board.vo;

import com.jakduk.core.model.embedded.BoardCommentStatus;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 13 오후 11:19
 */

@ApiModel(value = "자유게시판 댓글")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
