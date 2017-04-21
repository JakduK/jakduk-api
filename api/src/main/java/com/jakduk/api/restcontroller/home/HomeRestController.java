package com.jakduk.api.restcontroller.home;

import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.restcontroller.home.vo.GalleryOnHome;
import com.jakduk.api.restcontroller.home.vo.HomeResponse;
import com.jakduk.api.restcontroller.home.vo.LatestPost;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.Encyclopedia;
import com.jakduk.core.model.simple.GallerySimple;
import com.jakduk.core.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 20 오후 9:13
 */

@Api(tags = "Home", description = "홈 API")
@RestController
@RequestMapping("/api")
public class HomeRestController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private BoardFreeService boardFreeService;

    @Resource
    private ApiUtils apiUtils;

    @ApiOperation(value = "랜덤하게 백과사전 하나 가져오기")
    @RequestMapping(value = "/home/encyclopedia", method = RequestMethod.GET)
    public Encyclopedia getEncyclopediaWithRandom(@RequestParam(required = false) String lang,
                                                  Locale locale) {

        String language = CoreUtils.getLanguageCode(locale, lang);

        return homeService.getEncyclopediaWithRandom(language);
    }

    @ApiOperation(value = "홈에서 보여줄 각종 최근 데이터 가져오기")
    @GetMapping("/home/latest")
    public HomeResponse getLatest(
            @RequestParam(required = false) String lang,
            Locale locale) {

        String language = CoreUtils.getLanguageCode(locale, lang);

        HomeResponse response = new HomeResponse();
        response.setHomeDescription(homeService.getHomeDescription());
        response.setUsers(homeService.getUsersLatest(language));
        response.setComments(homeService.getBoardCommentsLatest());

        // 최근 게시물
        List<LatestPost> latestPosts = boardFreeService.getFreeLatest();
        response.setPosts(latestPosts);

        /*
        최근 사진
         */
        List<GallerySimple> galleries =  homeService.getGalleriesLatest();

        // 사진 경로 붙히기.
        List<GalleryOnHome> galleriesOfHome = galleries.stream()
                .map(GalleryOnHome::new)
                .map(gallery -> {
                    gallery.setImageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
                    gallery.setThumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));
                    return gallery;
                })
                .collect(Collectors.toList());

        response.setGalleries(galleriesOfHome);

        return response;
    }
}

