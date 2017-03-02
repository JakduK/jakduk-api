package com.jakduk.core.common.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by pyohwan on 16. 11. 8.
 */

public class CoreUtils {

    public static String getLanguageCode(Locale locale, String lang) {

        String getLanguage = Locale.ENGLISH.getLanguage();

        if (StringUtils.isEmpty(lang))
            lang = locale.getLanguage();

        if (StringUtils.isNotEmpty(lang) && lang.contains(Locale.KOREAN.getLanguage()))
            getLanguage = Locale.KOREAN.getLanguage();

        return getLanguage;
    }

    /**
     * ResourceBundle에서 메시지 가져오기.
     *
     * @param bundle 번들 이름 ex) messages.common
     * @param getString 메시지 이름 ex) common.exception.you.are.writer
     * @param params 파라미터가 있을 경우 계속 넣을 수 있음
     * @return 언어별 메시지 결과 반환
     */
    public static String getResourceBundleMessage(String bundle, String getString, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle, locale);
        return MessageFormat.format(resourceBundle.getString(getString), params);
    }

    public static String getExceptionMessage(String getString, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages.exception", locale);
        return MessageFormat.format(resourceBundle.getString(getString), params);
    }

    /**
     * HTML TAG를 제거한다.
     */
    public static String stripHtmlTag(String htmlTag) {
        String content = StringUtils.defaultIfBlank(htmlTag, "");
        content = Jsoup.parse(content).text();

        return content;
    }

}
