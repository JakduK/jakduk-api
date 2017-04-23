package com.jakduk.api.vo.board;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 3..
 */

@Getter
@Setter
public class FreePost {

    @ApiModelProperty(example = "58b7b9dd716dce06b10e449a", value = "글ID")
    private String id;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "글제목입니다.", value = "글제목")
    private String subject;

    @ApiModelProperty(example = "2", value = "글번호")
    private int seq;

    @ApiModelProperty(example = "FREE", value = "말머리")
    private CoreConst.BOARD_CATEGORY_TYPE category;

    @ApiModelProperty(example = "10", value = "읽음 수")
    private int views = 0;

    @ApiModelProperty(value = "글상태")
    private BoardStatus status;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardGallerySimple> galleries;

    @ApiModelProperty(example = "본문입니다.", value = "본문 100자")
    private String shortContent;

    @ApiModelProperty(example = "5", value = "댓글 수")
    private int commentCount;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private int likingCount;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private int dislikingCount;

}
