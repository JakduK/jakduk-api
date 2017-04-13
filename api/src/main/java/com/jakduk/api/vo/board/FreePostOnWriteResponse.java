package com.jakduk.api.vo.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 *         16. 7. 18 오후 9:19
 */

@ApiModel(value = "자유게시판 글쓰기 & 글 편집")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FreePostOnWriteResponse {

    @ApiModelProperty(value = "글 번호")
    private Integer seq;
}
