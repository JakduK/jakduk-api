package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.aggregate.BoardTop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 자유게시판 주간 선두 글
 *
 * @author pyohwan
 *         16. 7. 11 오후 10:26
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetArticlesTopsResponse {

    private List<BoardTop> topLikes; // 주간 좋아요수 선두
    private List<BoardTop> topComments; // 주간 댓글수 선두
}
