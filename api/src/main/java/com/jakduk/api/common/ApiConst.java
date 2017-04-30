package com.jakduk.api.common;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

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
     * 사진 상세에서 해당 사진의 앞뒤 사진의 갯수
     */
    public final static Integer NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY=10;


    /**
     * 읽음 중복 방지를 위한 쿠키
     */
    public enum VIEWS_COOKIE_TYPE {
        FREE_BOARD,
        GALLERY
    }

    /**
     * SNS 가입시 임시로 저장할 프로필를 담을 세션
     * {@link org.springframework.social.connect.web.ProviderSignInAttempt#SESSION_ATTRIBUTE}
     */
    public final static String PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE = "ProviderSignInAttempt";
}
