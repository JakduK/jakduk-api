package com.jakduk.model.web.jakdu;

import lombok.Data;

/**
 * Created by pyohwan on 16. 3. 13.
 */

@Data
public class JakduCommentWriteRequest {
    private String contents;            // 댓글 내용
    private String id;                  // 작두일정 ID
    private String device;              // 모바일 기기 정보

}
