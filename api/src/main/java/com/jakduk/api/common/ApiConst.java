package com.jakduk.api.common;

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
     * RSS, Sitemap 에서 한번에 읽을 아이템 수
     */
    public final static Integer NUMBER_OF_ITEMS_EACH_PAGES = 1000;

    /**
     * 읽음 중복 방지를 위한 쿠키
     */
    public enum VIEWS_COOKIE_TYPE {
        FREE_BOARD,
        GALLERY
    }
}
