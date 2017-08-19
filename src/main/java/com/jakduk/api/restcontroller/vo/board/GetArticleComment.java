package com.jakduk.api.restcontroller.vo.board;


import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.ArticleCommentStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleOnSearch;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 13 오후 11:19
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetArticleComment {

    @ApiModelProperty(value = "댓글ID")
    private String id;

    @ApiModelProperty(value = "연동 글")
    private ArticleOnSearch article;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "댓글상태")
    private ArticleCommentStatus status;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private Integer numberOfDislike;

    @ApiModelProperty(example = "LIKE", value = "나의 감정 표현 종류")
    private Constants.FEELING_TYPE myFeeling;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardGallerySimple> galleries;

}
