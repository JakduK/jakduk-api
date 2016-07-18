package com.jakduk.common;

import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pyohwan
 *         16. 7. 17 오후 8:16
 */
public class ApiUtils {

    /**
     * 쿠키를 저장한다. 이미 있다면 저장하지 않는다.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param prefix
     * @param id
     * @return 쿠키를 새로 저장했다면 true, 아니면 false.
     */
    public static boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, ApiConst.COOKIE_VIEWS_TYPE prefix, String id) {

        boolean findSameCookie = false;
        String cookieName = prefix + "_" + id;

        Cookie cookies[] = request.getCookies();

        if (cookies != null) {
            for (int i = 0 ; i < cookies.length ; i++) {
                String name = cookies[i].getName();

                if (cookieName.equals(name)) {
                    findSameCookie = true;
                    break;
                }
            }
        }

        if (!findSameCookie) {
            Cookie cookie = new Cookie(cookieName, "r");
            cookie.setMaxAge(ApiConst.BOARD_COOKIE_EXPIRE_SECONDS);
            response.addCookie(cookie);

            return true;
        } else {
            return false;
        }
    }

    /**
     * URL 생성
     * @param request HttpServletRequest
     * @param uri URI
     * @return 만들어진 URL
     */
    public static String buildFullRequestUrl(HttpServletRequest request, String uri) {
        return UrlUtils.buildFullRequestUrl(
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath() + uri, null);
    }
}
