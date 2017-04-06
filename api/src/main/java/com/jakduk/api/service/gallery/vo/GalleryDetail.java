package com.jakduk.api.service.gallery.vo;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.embedded.GalleryStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 4. 3..
 */

@Getter
@Setter
public class GalleryDetail {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "사진 ID")
    private String id;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "성남FC 시즌권 사진", value = "사진 이름")
    private String name;

    @ApiModelProperty(example = "https://staging.jakduk.com:8080/gallery/58b9050b807d714eaf50a111", value = "사진 풀 URL")
    private String imageUrl;

    @ApiModelProperty(example = "https://staging.jakduk.com:8080/gallery/thumbnail/58b9050b807d714eaf50a111", value = "사진 썸네일 URL")
    private String thumbnailUrl;

    @ApiModelProperty(example = "10", value = "읽음 수")
    private int views;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private Integer numberOfDislike;

    @ApiModelProperty(value = "사진 상태")
    private GalleryStatus status;

    @ApiModelProperty(example = "LIKE", value = "나의 감정 표현 종류")
    private CoreConst.FEELING_TYPE myFeeling;

}
