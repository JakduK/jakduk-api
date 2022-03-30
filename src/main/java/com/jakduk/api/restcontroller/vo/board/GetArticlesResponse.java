package com.jakduk.api.restcontroller.vo.board;

import java.util.List;
import java.util.Map;

/**
 * 자유게시판 글 목록
 *
 * @author pyohwan
 *         16. 7. 10 오후 11:52
 */

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

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }

    public List<GetArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<GetArticle> articles) {
        this.articles = articles;
    }

    public List<GetArticle> getNotices() {
        return notices;
    }

    public void setNotices(List<GetArticle> notices) {
        this.notices = notices;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
