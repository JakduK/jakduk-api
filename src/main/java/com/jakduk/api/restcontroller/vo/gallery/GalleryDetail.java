package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
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

    @ApiModelProperty(example = "https://dev-api.jakduk.com//gallery/58b9050b807d714eaf50a111", value = "사진 풀 URL")
    private String imageUrl;

    @ApiModelProperty(example = "https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111", value = "사진 썸네일 URL")
    private String thumbnailUrl;

    @ApiModelProperty(value = "사진 상태")
    private GalleryStatus status;

}
