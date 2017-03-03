package com.jakduk.api.restcontroller.home.vo;

import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pyohwan
 *         16. 7. 21 오후 9:47
 */

@ApiModel(description = "최근 사진")
@Getter
@Setter
public class GalleryOnHome {

    @ApiModelProperty(value = "사진 ID")
    private String id;

    @ApiModelProperty(value = "사진 이름")
    private String name;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "읽음 수")
    private int views = 0;

    @ApiModelProperty(value = "사진 URL")
    private String imageUrl;

    @ApiModelProperty(value = "썸네일 URL")
    private String thumbnailUrl;

    public GalleryOnHome(GalleryOnList gallery) {
        this.id = gallery.getId();
        this.name = gallery.getName();
        this.writer = gallery.getWriter();
        this.views = gallery.getViews();
    }
}
