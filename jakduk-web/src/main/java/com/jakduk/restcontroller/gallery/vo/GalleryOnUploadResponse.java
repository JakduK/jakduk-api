package com.jakduk.restcontroller.gallery.vo;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.embedded.GalleryStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 18 오후 9:31
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel(value = "사진 올리기 응답 객체")
public class GalleryOnUploadResponse {

    @ApiModelProperty(value = "사진 ID")
    private String id;

    @ApiModelProperty(value = "사진 이름")
    private String name;

    @ApiModelProperty(value = "사진 파일 이름")
    private String fileName;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(value = "사진 크기")
    private long size;

    @ApiModelProperty(value = "사진 파일 크기")
    private long fileSize;

    @ApiModelProperty(value = "사진 ContentType")
    private String contentType;

    @ApiModelProperty(value = "사진 상태")
    private GalleryStatus status;

    @ApiModelProperty(value = "사진 URL")
    private String imageUrl;

    @ApiModelProperty(value = "썸네일 URL")
    private String thumbnailUrl;
}
