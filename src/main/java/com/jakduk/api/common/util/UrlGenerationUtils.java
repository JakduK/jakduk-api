package com.jakduk.api.common.util;

import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.JakdukProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class UrlGenerationUtils {

    @Resource private JakdukProperties jakdukProperties;
    @Resource private JakdukProperties.ApiUrlPath apiUrlPathProperties;

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
     * 사진첩 이미지 URL을 생성한다.
     *
     * @param sizeType size 타입
     * @param id Gallery ID
     */
    public String generateGalleryUrl(Constants.IMAGE_SIZE_TYPE sizeType, String id) {

        if (StringUtils.isBlank(id))
            return null;

        String urlPathGallery = null;

        switch (sizeType) {
            case LARGE:
                urlPathGallery = apiUrlPathProperties.getGalleryImage();
                break;
            case SMALL:
                urlPathGallery = apiUrlPathProperties.getGalleryThumbnail();
                break;
        }

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(jakdukProperties.getApiServerUrl())
                .path("/{urlPathGallery}/{id}")
                .buildAndExpand(urlPathGallery, id);

        return uriComponents.toUriString();
    }
}
