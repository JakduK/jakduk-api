package com.jakduk.api.restcontroller.vo.board;


import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleSimple;
import lombok.*;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 13 오후 11:19
 */


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetArticleComment {

    private String id; // 댓글ID
    private ArticleSimple article; // 연동 글
    private CommonWriter writer; // 글쓴이
    private String content; // 내용
    private Integer numberOfLike; // 좋아요 수
    private Integer numberOfDislike; // 싫어요 수
    private Constants.FEELING_TYPE myFeeling; // 나의 감정 표현 종류
    private List<BoardGallerySimple> galleries; // 그림 목록
    private List<ArticleCommentLog> logs; // 로그

}
