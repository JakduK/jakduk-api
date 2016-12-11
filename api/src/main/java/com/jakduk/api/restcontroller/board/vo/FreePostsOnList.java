package com.jakduk.api.restcontroller.board.vo;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.BoardImage;
import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.simple.BoardFreeOnList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 11 오후 9:25
 */

@ApiModel(value = "자유게시판 게시물")
@Getter
@Setter
public class FreePostsOnList {

    @ApiModelProperty(value = "글ID")
    private String id;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "글제목")
    private String subject;

    @ApiModelProperty(value = "글번호")
    private int seq;

    @ApiModelProperty(value = "말머리")
    private CoreConst.BOARD_CATEGORY_TYPE category;

    @ApiModelProperty(value = "읽음 수")
    private int views;

    @ApiModelProperty(value = "글상태")
    private BoardStatus status;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardImage> galleries;

    @ApiModelProperty(value = "댓글 수")
    private int commentCount;

    @ApiModelProperty(value = "좋아요 수")
    private int likingCount;

    @ApiModelProperty(value = "싫어요 수")
    private int dislikingCount;

    public FreePostsOnList(BoardFreeOnList posts) {
        this.id = posts.getId();
        this.seq = posts.getSeq();
        this.writer = posts.getWriter();
        this.status = posts.getStatus();
        this.subject = posts.getSubject();
        this.category = posts.getCategory();
        this.views = posts.getViews();
        this.galleries = posts.getGalleries();
    }
}
