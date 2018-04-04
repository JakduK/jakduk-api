package com.jakduk.api.restcontroller.vo.board;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 3. 5..
 */

@Builder
@Getter
public class ArticleGallery {

    private String id; // 사진 ID
    private String name; // 사진 이름
    private String imageUrl; // 사진 풀 URL
    private String thumbnailUrl; // 사진 썸네일 URL

}
