package com.jakduk.core.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.core.common.CoreConst;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Created by pyohwan on 16. 11. 28.
 */
public class SearchUtils {

    /**
     * jackson ObjectMapper 반환
     */
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    /**
     * HTML TAG를 제거한다.
     */
    public static String stripHtmlTag(String htmlTag) {
        String content = Optional.ofNullable(htmlTag).orElse("");
        content = StringUtils.replacePattern(content, CoreConst.REGEX_FIND_HTML_TAG, "");
        content = StringUtils.replacePattern(content, CoreConst.REGEX_FIND_HTML_WHITESPACE, "");

        return content;
    }
}
