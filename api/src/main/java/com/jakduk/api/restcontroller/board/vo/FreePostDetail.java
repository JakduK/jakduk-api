package com.jakduk.api.restcontroller.board.vo;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.embedded.BoardHistory;
import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 15 오후 10:24
 */

@Getter
@Setter
public class FreePostDetail {

    @ApiModelProperty(example = "58b7b9dd716dce06b10e449a", value = "글ID")
    private String id;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "글제목입니다.", value = "글제목")
    private String subject;

    @ApiModelProperty(example = "2", value = "글번호")
    private int seq;

    @ApiModelProperty(example = "본문입니다", value = "본문")
    private String content;

    @ApiModelProperty(value = "말머리")
    private BoardCategory category;

    @ApiModelProperty(example = "10", value = "읽음 수")
    private int views;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private Integer numberOfDislike;

    @ApiModelProperty(value = "글상태")
    private BoardStatus status;

    @ApiModelProperty(value = "히스토리")
    private List<BoardHistory> history;

    @ApiModelProperty(value = "그림 목록")
    private List<FreePostDetailGallery> galleries;

    @ApiModelProperty(example = "LIKE", value = "나의 감정 표현 종류")
    private CoreConst.FEELING_TYPE myFeeling;

}
