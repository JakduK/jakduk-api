package com.jakduk.api.restcontroller;

import io.swagger.annotations.ApiModel;
import org.springframework.util.ObjectUtils;

/**
 * @author pyohwan
 * 16. 7. 8 오전 12:55
 */

@ApiModel(value = "JSON 타입의 빈 객체")
public class EmptyJsonResponse {

    private static EmptyJsonResponse emptyJsonResponse;

    public static EmptyJsonResponse newInstance() {

        if (ObjectUtils.isEmpty(emptyJsonResponse))
            emptyJsonResponse = new EmptyJsonResponse();

        return emptyJsonResponse;
    }
}
