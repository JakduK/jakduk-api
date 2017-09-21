package com.jakduk.api.restcontroller.vo.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwanjang on 2017. 3. 3..
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BoardGallerySimple {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "사진 ID")
    private String id;

    @ApiModelProperty(example = "https://dev-web.jakduk.com/api/gallery/thumbnail/58b9050b807d714eaf50a111", value = "썸네일 URL")
    private String thumbnailUrl;

}
