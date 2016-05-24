package com.jakduk.vo;

import lombok.Data;

/**
 * Created by pyohwan on 16. 3. 13.
 */

@Data
public class BoardCommentRequest {
    private Integer seq;                // seq
    private String contents;            // 댓글 내용
}
