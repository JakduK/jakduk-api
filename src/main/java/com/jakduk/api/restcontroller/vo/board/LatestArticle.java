package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.embedded.CommonWriter;
import lombok.*;

import java.util.List;

/**
 * 글쓴이의 최근글
 *
 * Created by pyohwanjang on 2017. 3. 10..
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LatestArticle {

    private String id; // 글ID
    private Integer seq; // 글번호
    private CommonWriter writer; // 글쓴이
    private String subject; // 글제목
    private List<BoardGallerySimple> galleries; // 그림 목록

}
