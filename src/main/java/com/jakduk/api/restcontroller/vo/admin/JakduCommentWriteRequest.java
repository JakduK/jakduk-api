package com.jakduk.api.restcontroller.vo.admin;

import com.jakduk.api.common.JakdukConst;
import lombok.Data;

/**
 * @author pyohwan
 * 16. 3. 13 오후 11:14
 */

@Data
public class JakduCommentWriteRequest {
    private String contents;            // 댓글 내용
    private String id;                  // 작두일정 ID
    private JakdukConst.DEVICE_TYPE device;              // 모바일 기기 정보

}
