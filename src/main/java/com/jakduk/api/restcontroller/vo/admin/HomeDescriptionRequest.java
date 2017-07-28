package com.jakduk.api.restcontroller.vo.admin;

import lombok.Data;

/**
 * Created by pyohwan on 16. 5. 15.
 */

@Data
public class HomeDescriptionRequest {

    private String desc;        // 내용

    private Integer priority;   // 우선 순위
}
