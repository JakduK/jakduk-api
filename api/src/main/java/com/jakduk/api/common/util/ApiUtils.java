package com.jakduk.api.common.util;

import com.jakduk.api.common.ApiConst;
import com.jakduk.core.common.CommonConst;
import org.springframework.mobile.device.Device;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author pyohwan
 *         16. 7. 17 오후 8:16
 */
public class ApiUtils {

    /**
     * 쿠키를 저장한다. 이미 있다면 저장하지 않는다.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param prefix 쿠키 이름의 Prefix
     * @param id 쿠키 이름에 쓰일 고유 ID
     * @return 쿠키를 새로 저장했다면 true, 아니면 false.
     */
    public static boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, ApiConst.VIEWS_COOKIE_TYPE prefix, String id) {

        String cookieName = prefix + "_" + id;
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> findCookie = Objects.isNull(cookies) ? Optional.empty() : Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny();

        if (! findCookie.isPresent()) {
            Cookie cookie = new Cookie(cookieName, "r");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(ApiConst.VIEWS_COOKIE_EXPIRE_SECONDS);
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

    /**
     * 모바일 디바이스 정보 가져오기.
     * @param device Device 객체
     * @return CommonConst.DEVICE_TYPE enum 타입
     */
    public static CommonConst.DEVICE_TYPE getDeviceInfo(Device device) {
        if (device.isNormal()) {
            return CommonConst.DEVICE_TYPE.NORMAL;
        } else if (device.isMobile()) {
            return CommonConst.DEVICE_TYPE.MOBILE;
        } else if (device.isTablet()) {
            return CommonConst.DEVICE_TYPE.TABLET;
        } else {
            return CommonConst.DEVICE_TYPE.NORMAL;
        }
    }
}
