package com.jakduk.api.restcontroller.vo.admin;

/**
 * Created by pyohwan on 16. 5. 15.
 */

public class HomeDescriptionRequest {

    private String desc;        // 내용
    private Integer priority;   // 우선 순위

    public String getDesc() {
        return desc;
    }

    public Integer getPriority() {
        return priority;
    }
}
