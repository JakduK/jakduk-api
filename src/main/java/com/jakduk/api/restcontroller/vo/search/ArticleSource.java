package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 4. 8..
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArticleSource {
    private String id; // 글ID
    private Integer seq; // 글번호
    private String board; // 게시판 ID
    private String category; // 말머리
    private CommonWriter writer; // 글쓴이
    private List<BoardGallerySimple> galleries; // 그림 목록
    private Float score; // 매칭 점수
    private Map<String, List<String>> highlight; // 매칭 단어 하이라이트
}
