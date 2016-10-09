package com.jakduk.api.restcontroller.board.vo;

import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.BoardHistory;
import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 15 오후 10:24
 */

@ApiModel(value = "자유게시판 글 상세")
@Getter
@Setter
public class FreePostOnDetail {

    @ApiModelProperty(value = "글 ID")
    private String id;

    @ApiModelProperty(value = "글 번호")
    private int seq;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "글 제목")
    private String subject;

    @ApiModelProperty(value = "글 내용")
    private String content;

    @ApiModelProperty(value = "말머리")
    private BoardCategory category;

    @ApiModelProperty(value = "읽음 수")
    private int views;

    @ApiModelProperty(value = "좋아요 회원 목록")
    private List<CommonFeelingUser> usersLiking;

    @ApiModelProperty(value = "싫어요 회원 목록")
    private List<CommonFeelingUser> usersDisliking;

    @ApiModelProperty(value = "글 상태")
    private BoardStatus status;

    @ApiModelProperty(value = "글 이력")
    private List<BoardHistory> history;

    @ApiModelProperty(value = "글 사진")
    private List<Gallery> galleries;

}
