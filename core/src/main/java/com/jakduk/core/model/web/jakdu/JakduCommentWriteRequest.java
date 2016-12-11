package com.jakduk.core.model.web.jakdu;

import com.jakduk.core.common.CoreConst;
import lombok.Data;

/**
 * @author pyohwan
 * 16. 3. 13 오후 11:14
 */

@Data
public class JakduCommentWriteRequest {
    private String contents;            // 댓글 내용
    private String id;                  // 작두일정 ID
    private CoreConst.DEVICE_TYPE device;              // 모바일 기기 정보

}
