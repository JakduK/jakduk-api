package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 3..
 */

@Getter
@Setter
public class LatestHomeArticle {

    @ApiModelProperty(example = "58b7b9dd716dce06b10e449a", value = "글ID")
    private String id;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "글제목입니다.", value = "글제목")
    private String subject;

    @ApiModelProperty(example = "2", value = "글번호")
    private Integer seq;

    @ApiModelProperty(example = "FREE", value = "게시판")
    private String board;

    @ApiModelProperty(example = "CLASSIC", value = "말머리")
    private String category;

    @ApiModelProperty(example = "10", value = "읽음 수")
    private Integer views;

    @ApiModelProperty(value = "글상태")
    private ArticleStatus status;

    @ApiModelProperty(value = "그림 목록")
    private List<BoardGallerySimple> galleries;

    @ApiModelProperty(example = "본문입니다.", value = "본문 100자")
    private String shortContent;

}
