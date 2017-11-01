package com.jakduk.api.restcontroller.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 글/댓글 쓰기 시 사진 연동
 *
 * @author pyohwan
 *         16. 7. 19 오후 9:30
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GalleryOnBoard {
    private String id; // 사진 ID
    private String name; // 사진 이름
}
