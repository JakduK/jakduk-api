package com.jakduk.api.restcontroller.vo.admin;

import com.jakduk.api.model.db.JakduComment;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 23 오후 11:14
 */

public class JakduCommentsResponse {
    private List<JakduComment> comments;
    private Integer count;

    public JakduCommentsResponse() {
    }

    public JakduCommentsResponse(List<JakduComment> comments, Integer count) {
        this.comments = comments;
        this.count = count;
    }

    public List<JakduComment> getComments() {
        return comments;
    }

    public Integer getCount() {
        return count;
    }
}
