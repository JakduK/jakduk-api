package com.jakduk.api.restcontroller.vo.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 23 오후 11:18
 */

@ApiModel(description = "특정 글의 댓글 목록")
@Builder
@Getter
public class GetArticleDetailCommentsResponse {

    @ApiModelProperty(value = "댓글 목록")
    private List<GetArticleComment> comments;

    @ApiModelProperty(example = "10", value = "댓글 수")
    private Integer count;
}
