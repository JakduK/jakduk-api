package com.jakduk.restcontroller;

import io.swagger.annotations.ApiModel;

/**
 * @author pyohwan
 * 16. 7. 8 오전 12:55
 */

@ApiModel(value = "JSON 타입의 빈 객체")
public class EmptyJsonResponse {

    public static EmptyJsonResponse newInstance() {
        return new EmptyJsonResponse();
    }
}
