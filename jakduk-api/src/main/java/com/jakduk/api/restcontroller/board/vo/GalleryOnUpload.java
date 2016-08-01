package com.jakduk.api.restcontroller.board.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pyohwan
 *         16. 7. 19 오후 9:30
 */


@Getter
@Setter
@ApiModel(value = "글쓰기 시 사용하는 이미지 객체")
public class GalleryOnUpload {

    @ApiModelProperty(value = "사진 ID")
    private String id;

    @ApiModelProperty(value = "사진 이름")
    private String name;

    @ApiModelProperty(value = "사진 파일 이름")
    private String fileName;

    @ApiModelProperty(value = "사진 크기")
    private long size;

}
