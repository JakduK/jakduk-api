package com.jakduk.api.restcontroller.vo.gallery;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 사진 목록 응답 객체
 *
 * @author pyohwan
 * 16. 5. 8 오후 11:22
 */

@Builder
@Getter
public class GalleriesResponse {
    private List<GalleryOnList> galleries; // 사진 목록
}
