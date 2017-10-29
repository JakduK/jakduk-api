package com.jakduk.api.restcontroller.vo.board;


import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 15 오후 10:24
 */

@Getter
@Setter
public class ArticleDetail {

    private String id; // 글ID
    private String board; // 게시판 ID
    private CommonWriter writer; // 글쓴이
    private String subject; // 글제목
    private Integer seq; // 글번호
    private String content; // 본문
    private BoardCategory category; // 말머리
    private Integer views; // 읽음 수
    private Integer numberOfLike; // 좋아요 수
    private Integer numberOfDislike; // 싫어요 수
    private ArticleStatus status; // 글상태
    private List<ArticleLog> logs; // 로그
    private List<ArticleGallery> galleries; // 그림 목록
    private Constants.FEELING_TYPE myFeeling; // 나의 감정 표현 종류

}
