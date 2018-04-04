package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pyohwan
 *         16. 7. 18 오후 9:31
 */

@Setter
@Getter
public class GalleryUploadResponse {

    private String id; // 사진 ID
    private String name; // 사진 이름"
    private String fileName; // 사진 파일 이름
    private CommonWriter writer; // 올린이
    private long size; // 사진 크기
    private long fileSize; // 사진 파일 크기
    private String contentType; // 사진 ContentType
    private GalleryStatus status; // 사진 상태
    private String imageUrl; // 사진 URL
    private String thumbnailUrl; // 썸네일 URL

}
