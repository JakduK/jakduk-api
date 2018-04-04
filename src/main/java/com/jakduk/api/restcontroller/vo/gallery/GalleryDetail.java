package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 4. 3..
 */

@Getter
@Setter
public class GalleryDetail {

    private String id; // 사진 ID
    private CommonWriter writer; // 올린이
    private String name; // 사진 이름
    private String imageUrl; // 사진 풀 URL
    private String thumbnailUrl; // 사진 썸네일 URL
    private GalleryStatus status; // 사진 상태

}
