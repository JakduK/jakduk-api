package com.jakduk.api.restcontroller.vo.board;

/**
 * 자유게시판 글쓰기 & 글 편집
 *
 * @author pyohwan
 *         16. 7. 18 오후 9:19
 */

public class WriteArticleResponse {
    private String board; // 게시판ID
    private Integer seq; // 글번호

    public WriteArticleResponse() {
    }

    public WriteArticleResponse(String board, Integer seq) {
        this.board = board;
        this.seq = seq;
    }

    public String getBoard() {
        return board;
    }

    public Integer getSeq() {
        return seq;
    }
}
