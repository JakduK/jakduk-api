package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 4. 8..
 */

@Getter
@Setter
public class BoardSource {

    @ApiModelProperty(example = "58b7b9dd716dce06b10e449a", value = "글ID")
    private String id;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "2", value = "글번호")
    private Integer seq;

    @ApiModelProperty(example = "FREE", value = "말머리")
    private String category;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardGallerySimple> galleries;

    @ApiModelProperty(example = "4.9219737", value = "매칭 점수")
    private Float score;

    @ApiModelProperty(value = "매칭 단어 하이라이트")
    private Map<String, List<String>> highlight;

}
