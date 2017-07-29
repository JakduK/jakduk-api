package com.jakduk.api.restcontroller.vo.admin;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.api.model.db.JakduComment;
import lombok.Data;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 23 오후 11:14
 */

@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
@Data
public class JakduCommentsResponse {
    private List<JakduComment> comments;
    private Integer count;
}
