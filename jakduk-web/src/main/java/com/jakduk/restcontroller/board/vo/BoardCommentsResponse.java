package com.jakduk.restcontroller.board.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.db.JakduComment;
import lombok.Data;

import java.util.List;

/**
 * Created by pyohwan on 16. 3. 23.
 */

@Data
@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
public class BoardCommentsResponse {
    private List<BoardFreeComment> comments;
    private Integer count;
}
