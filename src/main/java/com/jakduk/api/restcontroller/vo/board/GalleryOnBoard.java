package com.jakduk.api.restcontroller.vo.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 *         16. 7. 19 오후 9:30
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel(description = "글/댓글 쓰기 시 사진 연동")
public class GalleryOnBoard {

    @ApiModelProperty(example = "58b7b9dd716dce06b10e449a", value = "사진 ID")
    private String id;

    @ApiModelProperty(example = "공차는사진", value = "사진 제목")
    private String name;

}
