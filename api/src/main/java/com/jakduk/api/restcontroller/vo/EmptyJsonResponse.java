package com.jakduk.api.restcontroller.vo;

import io.swagger.annotations.ApiModel;

import java.util.Objects;

/**
 * @author pyohwan
 * 16. 7. 8 오전 12:55
 */

@ApiModel(description = "JSON 타입의 빈 객체")
public class EmptyJsonResponse {

    private static EmptyJsonResponse emptyJsonResponse;

    public static EmptyJsonResponse newInstance() {

        if (Objects.isNull(emptyJsonResponse))
            emptyJsonResponse = new EmptyJsonResponse();

        return emptyJsonResponse;
    }
}
