package com.jakduk.restcontroller.board.vo;

import com.jakduk.common.CommonConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 *         16. 7. 21 오후 10:54
 */

@ApiModel(value = "자유게시판 글 지움")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FreePostOnDeleteResponse {

    @ApiModelProperty(value = "결과")
    CommonConst.BOARD_DELETE_TYPE result;
}
