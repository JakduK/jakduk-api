package com.jakduk.api.restcontroller.vo.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 16 오후 7:55
 */

@ApiModel(value = "글쓰기/글고치기 폼")
@Getter
@Builder
public class WriteArticle {

    @ApiModelProperty(required = true, value = "글 제목", example = "글 제목입니다.")
    @Size(min = 1, max=60)
    @NotEmpty
    private String subject;

    @ApiModelProperty(required = true, value = "글 내용", example = "글 내용입니다.")
    @Size(min = 5)
    @NotEmpty
    private String content;

    @ApiModelProperty(required = true, value = "말머리 코드", example = "CLASSIC")
    private String categoryCode;

    @ApiModelProperty(value = "사진 목록")
    private List<GalleryOnBoard> galleries;

}
