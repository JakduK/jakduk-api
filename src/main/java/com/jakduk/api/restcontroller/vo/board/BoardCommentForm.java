package com.jakduk.api.restcontroller.vo.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 13 오후 11:05
 */

@Getter
@ApiModel(value = "댓글 달기 / 댓글 고치기 폼")
public class BoardCommentForm {

    @ApiModelProperty(value = "댓글 내용")
    @NotEmpty
    @Size(min = 1, max=800)
    private String content;

    @ApiModelProperty(value = "사진 목록")
    private List<GalleryOnBoard> galleries;

}
