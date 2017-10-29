package com.jakduk.api.restcontroller.vo;

import java.util.Objects;

/**
 * JSON 타입의 빈 객체
 *
 * @author pyohwan
 * 16. 7. 8 오전 12:55
 */

public class EmptyJsonResponse {

    private static EmptyJsonResponse emptyJsonResponse;

    public static EmptyJsonResponse newInstance() {

        if (Objects.isNull(emptyJsonResponse))
            emptyJsonResponse = new EmptyJsonResponse();

        return emptyJsonResponse;
    }
}
