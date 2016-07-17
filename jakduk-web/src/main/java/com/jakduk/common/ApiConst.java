package com.jakduk.common;

/**
 * @author pyohwan
 *         16. 7. 17 오후 8:21
 */
public class ApiConst {

    /**
     * 게시물 쿠키의 만료되는 시간(초)
     */
    public final static Integer BOARD_COOKIE_EXPIRE_SECONDS = 30;

    /**
     * 읽음 중복 방지를 위한 쿠키
     */
    public enum COOKIE_VIEWS_TYPE {
        FREE_BOARD,
        GALLERY
    }
}
