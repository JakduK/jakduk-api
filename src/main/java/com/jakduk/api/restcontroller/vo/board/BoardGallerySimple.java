package com.jakduk.api.restcontroller.vo.board;

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

    private String id; // 사진 ID
    private String thumbnailUrl; // 썸네일 URL

}
