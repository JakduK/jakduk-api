package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.model.db.Encyclopedia;
import com.jakduk.api.restcontroller.vo.home.HomeLatestItemsResponse;
import com.jakduk.api.service.ArticleService;
import com.jakduk.api.service.GalleryService;
import com.jakduk.api.service.HomeService;
import com.jakduk.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 홈 API
 *
 * @author pyohwan
 * 16. 3. 20 오후 9:13
 */

@RestController
@RequestMapping("/api")
public class HomeRestController {

    @Autowired private HomeService homeService;
    @Autowired private UserService userService;
    @Autowired private ArticleService articleService;
    @Autowired private GalleryService galleryService;

    // 랜덤하게 백과사전 하나 가져오기
    @GetMapping("/home/encyclopedia")
    public Encyclopedia getEncyclopediaWithRandom() {

        String language = JakdukUtils.getLanguageCode();

        return homeService.getEncyclopediaWithRandom(language);
    }

    // 홈에서 보여줄 각종 최근 데이터 가져오기
    @GetMapping("/home/latest")
    public HomeLatestItemsResponse getLatestItems() {

        String language = JakdukUtils.getLanguageCode();

        return HomeLatestItemsResponse.builder()
                .homeDescription(homeService.getHomeDescription())
                .users(userService.findSimpleUsers())
                .comments(articleService.getLatestComments())
                .articles(articleService.getLatestArticles())
                .galleries(galleryService.findSimpleById(null, Constants.HOME_SIZE_GALLERY))
                .build();
    }

}

