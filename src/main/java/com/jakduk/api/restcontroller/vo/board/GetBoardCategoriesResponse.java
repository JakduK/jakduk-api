package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.board.category.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 게시판 말머리 목록
 *
 * @author pyohwan
 *         16. 7. 18 오후 9:55
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetBoardCategoriesResponse {
    private List<BoardCategory> categories; // 말머리 목록
}
