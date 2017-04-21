package com.jakduk.api.vo.board;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.BoardCommentStatus;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.simple.BoardFreeOnSearch;
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
public class FreePostComment {

    @ApiModelProperty(value = "댓글ID")
    private String id;

    @ApiModelProperty(value = "연동 글")
    private BoardFreeOnSearch boardItem;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "댓글상태")
    private BoardCommentStatus status;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private Integer numberOfDislike;

    @ApiModelProperty(example = "LIKE", value = "나의 감정 표현 종류")
    private CoreConst.FEELING_TYPE myFeeling;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardGallerySimple> galleries;

}
