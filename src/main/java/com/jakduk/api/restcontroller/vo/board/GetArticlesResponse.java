package com.jakduk.api.restcontroller.vo.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 자유게시판 글 목록
 *
 * @author pyohwan
 *         16. 7. 10 오후 11:52
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetArticlesResponse {

    private Map<String, String> categories; // 말머리 맵
    private List<GetArticle> articles; // 글 목록
    private List<GetArticle> notices; // 공지글 목록
    private Boolean last; // 마지막 페이지 여부
    private Boolean first; // 첫 페이지 여부
    private Integer totalPages; // 전체 페이지 수
    private Integer size; // 페이지당 글 수
    private Integer number; // 현재 페이지(0부터 시작)
    private Integer numberOfElements; // 현제 페이지에서 글 수
    private Long totalElements; // 전체 글 수

}
