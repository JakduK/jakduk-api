package com.jakduk.api.vo.gallery;

import com.jakduk.api.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 4. 22..
 */

@ApiModel(description = "사진 객체")
@Getter
@Setter
public class GalleryOnList {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "사진 ID")
    private String id;

    @ApiModelProperty(example = "성남FC 시즌권 사진", value = "사진 이름")
    private String name;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "10", value = "읽음 수")
    private int views;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private int likingCount;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private int dislikingCount;

    @ApiModelProperty(example = "https://staging.jakduk.com:8080/gallery/58b9050b807d714eaf50a111", value = "사진 풀 URL")
    private String imageUrl;

    @ApiModelProperty(example = "https://staging.jakduk.com:8080/gallery/thumbnail/58b9050b807d714eaf50a111", value = "사진 썸네일 URL")
    private String thumbnailUrl;

}
