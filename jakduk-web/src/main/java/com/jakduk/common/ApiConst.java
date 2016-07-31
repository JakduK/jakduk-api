package com.jakduk.common;

/**
 * @author pyohwan
 *         16. 7. 17 오후 8:21
 */
public class ApiConst {

    /**
     * 읽음 수의 쿠키 만료 시간(초)
     */
    public final static Integer VIEWS_COOKIE_EXPIRE_SECONDS = 30;

    /**
     * 읽음 중복 방지를 위한 쿠키
     */
    public enum VIEWS_COOKIE_TYPE {
        FREE_BOARD,
        GALLERY
    }
}
