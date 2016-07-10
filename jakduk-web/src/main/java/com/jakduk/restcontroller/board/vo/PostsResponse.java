package com.jakduk.restcontroller.board.vo;

import com.jakduk.model.simple.BoardFreeOnList;
import lombok.*;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 10 오후 11:52
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostsResponse {

    List<BoardFreeOnList> posts;

    boolean last;
    boolean first;
    int totalPages;
    int size;
    int number;
    int numberOfElements;
    long totalElements;
}
