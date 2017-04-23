package com.jakduk.api.vo.board;

import com.jakduk.core.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 10..
 */

@ApiModel(description = "글쓴이의 최근글")
@Getter
@Setter
public class LatestFreePost {

    @ApiModelProperty(example = "58b7b9dd716dce06b10e449a", value = "글ID")
    private String id;

    @ApiModelProperty(example = "2", value = "글번호")
    private Integer seq;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "글제목입니다.", value = "글제목")
    private String subject;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardGallerySimple> galleries;

}
