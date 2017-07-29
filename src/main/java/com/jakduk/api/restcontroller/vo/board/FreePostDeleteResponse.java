package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.JakdukConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * @author pyohwan
 *         16. 7. 21 오후 10:54
 */

@ApiModel(value = "자유게시판 글 지움")
@Builder
@Getter
public class FreePostDeleteResponse {

    @ApiModelProperty(value = "결과")
    JakdukConst.BOARD_DELETE_TYPE result;

}
