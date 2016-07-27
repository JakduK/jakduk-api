package com.jakduk.restcontroller;

import io.swagger.annotations.ApiModel;

/**
 * Created by pyohwan on 16. 7. 8.
 */

@ApiModel(value = "JSON 타입의 빈 객체")
public class EmptyJsonResponse {

    public static EmptyJsonResponse newInstance() {
        return new EmptyJsonResponse();
    }
}
