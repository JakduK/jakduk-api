package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 4. 8..
 */

public class ArticleSource {
    private String id; // 글ID
    private Integer seq; // 글번호
    private String board; // 게시판 ID
    private String category; // 말머리
    private CommonWriter writer; // 글쓴이
    private List<BoardGallerySimple> galleries; // 그림 목록
    private Float score; // 매칭 점수
    private Map<String, List<String>> highlight; // 매칭 단어 하이라이트

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CommonWriter getWriter() {
        return writer;
    }

    public void setWriter(CommonWriter writer) {
        this.writer = writer;
    }

    public List<BoardGallerySimple> getGalleries() {
        return galleries;
    }

    public void setGalleries(List<BoardGallerySimple> galleries) {
        this.galleries = galleries;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Map<String, List<String>> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, List<String>> highlight) {
        this.highlight = highlight;
    }
}
