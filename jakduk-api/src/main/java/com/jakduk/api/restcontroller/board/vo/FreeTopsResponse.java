package com.jakduk.api.restcontroller.board.vo;

import com.jakduk.core.model.etc.BoardFreeOnBest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 11 오후 10:26
 */

@ApiModel(value = "자유게시판 주간 선두 글")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FreeTopsResponse {

    @ApiModelProperty(value = "주간 좋아요수 선두")
    private List<BoardFreeOnBest> topLikes;

    @ApiModelProperty(value = "주간 댓글수 선두")
    private List<BoardFreeOnBest> topComments;
}
