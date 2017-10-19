package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.model.db.Encyclopedia;
import com.jakduk.api.restcontroller.vo.home.GalleryOnHome;
import com.jakduk.api.restcontroller.vo.home.HomeResponse;
import com.jakduk.api.service.ArticleService;
import com.jakduk.api.service.GalleryService;
import com.jakduk.api.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 20 오후 9:13
 */

@Api(tags = "Home", description = "홈 API")
@RestController
@RequestMapping("/api")
public class HomeRestController {

    @Autowired private UrlGenerationUtils urlGenerationUtils;
    @Autowired private HomeService homeService;
    @Autowired private ArticleService articleService;
    @Autowired private GalleryService galleryService;

    @ApiOperation(value = "랜덤하게 백과사전 하나 가져오기")
    @GetMapping("/home/encyclopedia")
    public Encyclopedia getEncyclopediaWithRandom() {

        String language = JakdukUtils.getLanguageCode();

        return homeService.getEncyclopediaWithRandom(language);
    }

    @ApiOperation("홈에서 보여줄 각종 최근 데이터 가져오기")
    @GetMapping("/home/latest")
    public HomeResponse getLatest() {

        String language = JakdukUtils.getLanguageCode();

        // 최근 사진
        List<GalleryOnHome> galleries = galleryService.findSimpleById(null, Constants.HOME_SIZE_GALLERY).stream()
                .map(GalleryOnHome::new)
                .peek(gallery -> {
                    gallery.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
                    gallery.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));
                })
                .collect(Collectors.toList());

        return HomeResponse.builder()
                .homeDescription(homeService.getHomeDescription())
                .users(homeService.getUsersLatest(language))
                .comments(homeService.getBoardCommentsLatest())
                .articles(articleService.getLatestArticles())
                .galleries(galleries)
                .build();
    }
}

