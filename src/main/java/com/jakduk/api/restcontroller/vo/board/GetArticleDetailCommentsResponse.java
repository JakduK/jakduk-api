package com.jakduk.api.restcontroller.vo.board;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 특정 글의 댓글 목록
 *
 * @author pyohwan
 * 16. 3. 23 오후 11:18
 */

@Builder
@Getter
public class GetArticleDetailCommentsResponse {
    private List<GetArticleComment> comments; // 댓글 목록
    private Integer count; // 댓글 수
}
