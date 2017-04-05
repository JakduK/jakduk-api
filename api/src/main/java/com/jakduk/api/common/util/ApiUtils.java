package com.jakduk.api.common.util;

import com.jakduk.api.common.ApiConst;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author pyohwan
 *         16. 7. 17 오후 8:16
 */

@Component
public class ApiUtils {

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${api.gallery.image.url.path}")
    private String apiGalleryFullUrlPath;

    @Value("${api.gallery.thumbnail.url.path}")
    private String apiGalleryThumbnailUrlPath;

    /**
     * 쿠키를 저장한다. 이미 있다면 저장하지 않는다.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param prefix 쿠키 이름의 Prefix
     * @param id 쿠키 이름에 쓰일 고유 ID
     * @return 쿠키를 새로 저장했다면 true, 아니면 false.
     */
    public static Boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, ApiConst.VIEWS_COOKIE_TYPE prefix, String id) {

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
     *
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
     *
     * @param device Device 객체
     * @return CoreConst.DEVICE_TYPE enum 타입
     */
    public static CoreConst.DEVICE_TYPE getDeviceInfo(Device device) {
        if (device.isNormal()) {
            return CoreConst.DEVICE_TYPE.NORMAL;
        } else if (device.isMobile()) {
            return CoreConst.DEVICE_TYPE.MOBILE;
        } else if (device.isTablet()) {
            return CoreConst.DEVICE_TYPE.TABLET;
        } else {
            return CoreConst.DEVICE_TYPE.NORMAL;
        }
    }

    /**
     * 사진첩 이미지 URL을 생성한다.
     *
     * @param sizeType size 타입
     * @param id Gallery ID
     */
    public String generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE sizeType, String id) {

        if (StringUtils.isBlank(id))
            return null;

        String pictureUrl = null;

        switch (sizeType) {
            case LARGE:
                pictureUrl = String.format("%s/%s/%s", apiServerUrl, apiGalleryFullUrlPath, id);
                break;
            case SMALL:
                pictureUrl = String.format("%s/%s/%s", apiServerUrl, apiGalleryThumbnailUrlPath, id);
                break;
        }

        return pictureUrl;
    }

    /**
     * 좋아요, 싫어요 목록에서 나도 참여 하였는지 검사
     *
     * @param commonWriter 나
     * @param usersLiking 좋아요 목록
     * @param usersDisliking 싫어요 목록
     * @return 감정 표현
     */
    public static CoreConst.FEELING_TYPE getMyFeeling(CommonWriter commonWriter, List<CommonFeelingUser> usersLiking, List<CommonFeelingUser> usersDisliking) {
        if (Objects.nonNull(commonWriter)) {
            if (! ObjectUtils.isEmpty(usersLiking) && usersLiking.stream()
                    .anyMatch(commonFeelingUser -> commonFeelingUser.getUserId().equals(commonWriter.getUserId()))) {
                return CoreConst.FEELING_TYPE.LIKE;
            } else if (! ObjectUtils.isEmpty(usersDisliking) && usersDisliking.stream()
                    .anyMatch(commonFeelingUser -> commonFeelingUser.getUserId().equals(commonWriter.getUserId()))) {
                return CoreConst.FEELING_TYPE.DISLIKE;
            }
        }

        return null;
    }
}
