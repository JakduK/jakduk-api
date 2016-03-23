package com.jakduk.model.web.jakdu;

import com.jakduk.model.db.JakduComment;
import lombok.Data;

import java.util.List;

/**
 * Created by pyohwan on 16. 3. 23.
 */

@Data
public class JakduCommentsResponse {
    private List<JakduComment> comments;
    private Integer count;
}
