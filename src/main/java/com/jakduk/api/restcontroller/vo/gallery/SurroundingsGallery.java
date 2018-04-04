package com.jakduk.api.restcontroller.vo.gallery;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */

@Getter
@Setter
public class SurroundingsGallery {
    private String id; // 사진 ID
    private String name; // 사진 이름
    private String imageUrl; // 사진 풀 URL
    private String thumbnailUrl; // 사진 썸네일 URL
}
