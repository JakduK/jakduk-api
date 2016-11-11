package com.jakduk.api.restcontroller.board.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.core.model.db.BoardFreeComment;
import lombok.Data;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 23 오후 11:18
 */

@Data
@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
public class BoardCommentsResponse {
    private List<BoardFreeComment> comments;
    private Integer count;
}
