package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.aggregate.BoardPostTop;
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
public class GetBoardTopsResponse {

    @ApiModelProperty(value = "주간 좋아요수 선두")
    private List<BoardPostTop> topLikes;

    @ApiModelProperty(value = "주간 댓글수 선두")
    private List<BoardPostTop> topComments;
}
