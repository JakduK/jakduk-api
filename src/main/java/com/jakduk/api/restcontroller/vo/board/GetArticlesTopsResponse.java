package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.aggregate.BoardTop;

import java.util.List;

/**
 * 자유게시판 주간 선두 글
 *
 * @author pyohwan
 *         16. 7. 11 오후 10:26
 */

public class GetArticlesTopsResponse {

    private List<BoardTop> topLikes; // 주간 좋아요수 선두
    private List<BoardTop> topComments; // 주간 댓글수 선두

    public GetArticlesTopsResponse() {
    }

    public GetArticlesTopsResponse(List<BoardTop> topLikes, List<BoardTop> topComments) {
        this.topLikes = topLikes;
        this.topComments = topComments;
    }

    public List<BoardTop> getTopLikes() {
        return topLikes;
    }

    public List<BoardTop> getTopComments() {
        return topComments;
    }
}
