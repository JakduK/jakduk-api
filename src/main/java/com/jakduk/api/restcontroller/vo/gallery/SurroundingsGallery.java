package com.jakduk.api.restcontroller.vo.gallery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */

@Getter
@Setter
public class SurroundingsGallery {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "사진 ID")
    private String id;

    @ApiModelProperty(example = "성남FC 시즌권 사진", value = "사진 이름")
    private String name;

    @ApiModelProperty(example = "https://staging.jakduk.com:8080/gallery/58b9050b807d714eaf50a111", value = "사진 풀 URL")
    private String imageUrl;

    @ApiModelProperty(example = "https://staging.jakduk.com:8080/gallery/thumbnail/58b9050b807d714eaf50a111", value = "사진 썸네일 URL")
    private String thumbnailUrl;

    @ApiModelProperty(example = "10", value = "읽음 수")
    private int views;

}
