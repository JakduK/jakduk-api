package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.board.category.BoardCategory;

import java.util.List;

/**
 * 게시판 말머리 목록
 *
 * @author pyohwan
 *         16. 7. 18 오후 9:55
 */

public class GetBoardCategoriesResponse {
    private List<BoardCategory> categories; // 말머리 목록

    public GetBoardCategoriesResponse() {
    }

    public GetBoardCategoriesResponse(List<BoardCategory> categories) {
        this.categories = categories;
    }

    public List<BoardCategory> getCategories() {
        return categories;
    }
}
