package com.jakduk.api.common.util;

import com.jakduk.api.common.ApiConst;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.embedded.LocalName;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    @Value("${api.path.gallery.url.image}")
    private String apiGalleryFullUrlPath;

    @Value("${api.path.gallery.url.thumbnail}")
    private String apiGalleryThumbnailUrlPath;

    private final static String GALLERIES_FOR_REMOVAL = ":galleries_for_removal";

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

    /**
     * 세션에서 해당 아이템의 지워질 사진 ID 목록을 가져온다.
     *
     * @param request HttpServletRequest
     * @param from 출처
     * @param id Item ID
     */
    public static List<String> getSessionOfGalleryIdsForRemoval(HttpServletRequest request, CoreConst.GALLERY_FROM_TYPE from, String id) {
        String name = from + ":" + id + GALLERIES_FOR_REMOVAL;
        HttpSession httpSession = request.getSession();

        return  (List<String>) httpSession.getAttribute(name);
    }

    /**
     * 해당 아이템의 지워질 사진 ID 목록 세션을 지운다.
     *
     * @param request HttpServletRequest
     * @param from 출처
     * @param id Item ID
     */
    public static void removeSessionOfGalleryIdsForRemoval(HttpServletRequest request, CoreConst.GALLERY_FROM_TYPE from, String id) {
        String name = from + ":" + id + GALLERIES_FOR_REMOVAL;
        HttpSession httpSession = request.getSession();

        httpSession.removeAttribute(name);
    }

    /**
     * 글, 댓글 편집시 호출 했기 때문에 gallery를 바로 지우면 안된다. 글/댓글 편집 완료 시 실제로 gallery를 지워야 한다.
     * session에 저장해 두자.
     *
     * @param request HttpServletRequest
     * @param from 출처
     * @param itemId Item ID
     * @param galleryId Gallery ID
     */
    public static void setSessionOfGalleryIdsForRemoval(HttpServletRequest request, CoreConst.GALLERY_FROM_TYPE from, String itemId,
                                                        String galleryId) {

        HttpSession httpSession = request.getSession();
        String name = from + ":" + itemId + GALLERIES_FOR_REMOVAL;

        List<String> galleryIds = getSessionOfGalleryIdsForRemoval(request, from, itemId);

        if (ObjectUtils.isEmpty(galleryIds))
            galleryIds = new ArrayList<>();

        if (! galleryIds.contains(galleryId))
            galleryIds.add(galleryId);

        httpSession.setAttribute(name, galleryIds);
        httpSession.setMaxInactiveInterval(60*60); // 1 hour
    }

    /**
     * 임시 이메일 주소를 생선한다.
     */
    public static String generateTemporaryEmail() {
        return RandomStringUtils.randomAlphanumeric(5) + "@jakduk.com";
    }

    /**
     * 임시 이메일 주소인지 확인한다.
     */
    public static Boolean isTempararyEmail(String email) {
        return StringUtils.endsWith(email, "@jakduk.com");
    }

    /**
     * 해당 언어에 맞는 축구단 이름 가져오기.
     *
     * @param footballClub 축구단 객체
     * @param language 언어
     * @return LocalName 객체
     */
    public static LocalName getLocalNameOfFootballClub(FootballClub footballClub, String language) {
        LocalName localName = null;

        if (Objects.nonNull(footballClub)) {
            List<LocalName> names = footballClub.getNames();

            for (LocalName name : names) {
                if (name.getLanguage().equals(language)) {
                    localName = name;
                }
            }
        }

        return localName;
    }

}
