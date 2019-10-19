package com.jakduk.api.common.util;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.LocalName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author pyohwan
 *         16. 7. 17 오후 8:16
 */

public class JakdukUtils {

    private static Pattern PATTERN_WITHOUT_TAGS = Pattern.compile("</?(.+?)>");

    private final static String GALLERIES_FOR_REMOVAL = ":galleries_for_removal";

    private static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z')
            .build();

    /**
     * 사용 가능한 언어 코드를 클라이언트의 Locale 에서 뽑아온다.
     */
    public static String getLanguageCode() {
        return JakdukUtils.getLanguageCode(null);
    }

    /**
     * 사용 가능한 언어 코드를 클라이언트의 Locale 에서 뽑아온다.
     *
     * @param wantLanguage 원하는 language
     */
    public static String getLanguageCode(String wantLanguage) {

        Locale locale = LocaleContextHolder.getLocale();
        String returnLanguage = Locale.ENGLISH.getLanguage();

        if (StringUtils.isBlank(wantLanguage))
            wantLanguage = locale.getLanguage();

        if (wantLanguage.contains(Locale.KOREAN.getLanguage()))
            returnLanguage = Locale.KOREAN.getLanguage();

        return returnLanguage;
    }

    public static Locale getLocale() {
        return JakdukUtils.getLocale(null);
    }

    public static Locale getLocale(String wantLanguage) {
        Locale locale = LocaleContextHolder.getLocale();
        Locale returnLocale = Locale.US;

        if (StringUtils.isBlank(wantLanguage))
            wantLanguage = locale.getLanguage();

        if (wantLanguage.contains(Locale.KOREA.getLanguage()))
            returnLocale = Locale.KOREA;

        return returnLocale;
    }

    /**
     * ResourceBundle에서 메시지 가져오기.
     *
     * @param getString 메시지 이름 ex) common.exception.you.are.writer
     * @param params 파라미터가 있을 경우 계속 넣을 수 있음
     * @return 언어별 메시지 결과 반환
     */
    public static String getMessageSource(String getString, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        return MessageFormat.format(resourceBundle.getString(getString), params);
    }

    public static String getValidationMessage(String getString, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages", locale);
        return MessageFormat.format(resourceBundle.getString(getString), params);
    }

    /**
     * HTML TAG를 제거한다.
     */
    public static String stripHtmlTag(String html) {
        return PATTERN_WITHOUT_TAGS.matcher(html).replaceAll("");
    }

    /**
     * 쿠키를 저장한다. 이미 있다면 저장하지 않는다.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param prefix 쿠키 이름의 Prefix
     * @param id 쿠키 이름에 쓰일 고유 ID
     * @return 쿠키를 새로 저장했다면 true, 아니면 false.
     */
    public static Boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, Constants.VIEWS_COOKIE_TYPE prefix, String id) {

        String cookieName = prefix + "_" + id;
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> findCookie = Objects.isNull(cookies) ? Optional.empty() : Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny();

        if (! findCookie.isPresent()) {
            Cookie cookie = new Cookie(cookieName, "r");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(Constants.VIEWS_COOKIE_EXPIRE_SECONDS);
            response.addCookie(cookie);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 좋아요, 싫어요 목록에서 나도 참여 하였는지 검사
     *
     * @param commonWriter 나
     * @param usersLiking 좋아요 목록
     * @param usersDisliking 싫어요 목록
     * @return 감정 표현
     */
    public static Constants.FEELING_TYPE getMyFeeling(CommonWriter commonWriter, List<CommonFeelingUser> usersLiking, List<CommonFeelingUser> usersDisliking) {
        if (Objects.nonNull(commonWriter)) {
            if (! CollectionUtils.isEmpty(usersLiking) && usersLiking.stream()
                    .anyMatch(commonFeelingUser -> commonFeelingUser.getUserId().equals(commonWriter.getUserId()))) {
                return Constants.FEELING_TYPE.LIKE;
            } else if (! CollectionUtils.isEmpty(usersDisliking) && usersDisliking.stream()
                    .anyMatch(commonFeelingUser -> commonFeelingUser.getUserId().equals(commonWriter.getUserId()))) {
                return Constants.FEELING_TYPE.DISLIKE;
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
    public static List<String> getSessionOfGalleryIdsForRemoval(HttpServletRequest request, Constants.GALLERY_FROM_TYPE from, String id) {
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
    public static void removeSessionOfGalleryIdsForRemoval(HttpServletRequest request, Constants.GALLERY_FROM_TYPE from, String id) {
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
    public static void setSessionOfGalleryIdsForRemoval(HttpServletRequest request, Constants.GALLERY_FROM_TYPE from, String itemId,
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


        return generator.generate(5) + "@jakduk.com";
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

        if (StringUtils.isBlank(language))
            return null;

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
