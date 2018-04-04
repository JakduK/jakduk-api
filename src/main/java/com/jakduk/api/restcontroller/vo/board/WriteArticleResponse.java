package com.jakduk.api.restcontroller.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 자유게시판 글쓰기 & 글 편집
 *
 * @author pyohwan
 *         16. 7. 18 오후 9:19
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WriteArticleResponse {
    private String board; // 게시판ID
    private Integer seq; // 글번호
}
