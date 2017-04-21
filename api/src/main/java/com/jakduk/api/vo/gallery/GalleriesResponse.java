package com.jakduk.api.vo.gallery;

import com.jakduk.core.model.simple.GallerySimple;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.util.List;

/**
 * @author pyohwan
 * 16. 5. 8 오후 11:22
 */

@ApiModel(description = "사진 목록 응답 객체")
@Builder
public class GalleriesResponse {

    @ApiModelProperty(value = "사진 목록")
    private List<GallerySimple> galleries;

}
