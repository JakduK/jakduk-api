package com.jakduk.api.vo.gallery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author pyohwan
 * 16. 5. 8 오후 11:22
 */

@ApiModel(description = "사진 목록 응답 객체")
@Builder
@Getter
public class GalleriesResponse {

    @ApiModelProperty(value = "사진 목록")
    private List<GalleryOnList> galleries;

}
