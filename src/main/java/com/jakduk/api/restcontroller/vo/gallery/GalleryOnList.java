package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 4. 22..
 */

@Getter
@Setter
public class GalleryOnList {

    private String id; // 사진 ID
    private String name; // 사진 이름
    private CommonWriter writer; // 올린이
    private String imageUrl; // 사진 풀 URL
    private String thumbnailUrl; // 사진 썸네일 URL

}
