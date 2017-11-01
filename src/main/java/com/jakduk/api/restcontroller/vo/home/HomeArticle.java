package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import lombok.*;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 3..
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HomeArticle {

    private String id; // 글ID
    private CommonWriter writer; // 글쓴이
    private String subject; // 글제목
    private Integer seq; // 글번호
    private String board; // 게시판
    private String category; // 말머리
    private Integer views; // 읽음 수
    private ArticleStatus status; // 글상태
    private List<BoardGallerySimple> galleries; // 그림 목록
    private String shortContent; // 본문 100자

}
