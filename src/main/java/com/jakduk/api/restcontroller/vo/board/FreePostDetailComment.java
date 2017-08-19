package com.jakduk.api.restcontroller.vo.board;


import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.ArticleCommentStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 6..
 */

@ApiModel(description = "특정 글의 댓글")
@Getter
@Setter
public class FreePostDetailComment {

    @ApiModelProperty(example = "58bcfabce1948902af90acc4", value = "댓글 ID")
    private String id;

    @ApiModelProperty(value = "댓글 ID")
    private ArticleItem article;

    @ApiModelProperty(value = "댓글 상태")
    private ArticleCommentStatus status;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "댓글입니다.", value = "댓글 내용")
    private String content;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private Integer numberOfDislike;

    @ApiModelProperty(example = "LIKE", value = "나의 감정 표현 종류")
    private Constants.FEELING_TYPE myFeeling;

    @ApiModelProperty(value = "그림 목록")
    private List<ArticleGallery> galleries;

    @ApiModelProperty(value = "로그")
    private List<BoardFreeCommentLog> logs;

}
