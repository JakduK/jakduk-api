package com.jakduk.api.restcontroller.home;

import com.jakduk.api.restcontroller.home.vo.GalleryOnHome;
import com.jakduk.api.restcontroller.home.vo.HomeResponse;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.Encyclopedia;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.service.BoardFreeService;
import com.jakduk.core.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${core.gallery.image.path}")
    private String imagePath;

    @Value("${core.gallery.thumbnail.path}")
    private String thumbnailPath;

    @ApiOperation(value = "랜덤하게 백과사전 하나 가져오기")
    @RequestMapping(value = "/home/encyclopedia", method = RequestMethod.GET)
    public Encyclopedia getEncyclopediaWithRandom(@RequestParam(required = false) String lang,
                                                  Locale locale) {

        String language = CoreUtils.getLanguageCode(locale, lang);

        return homeService.getEncyclopediaWithRandom(language);
    }

    @ApiOperation(value = "홈에서 보여줄 각종 최근 데이터 가져오기")
    @RequestMapping(value = "/home/latest", method = RequestMethod.GET)
    public HomeResponse getLatest(@RequestParam(required = false) String lang,
                                  Locale locale) {

        String language = CoreUtils.getLanguageCode(locale, lang);

        HomeResponse response = new HomeResponse();
        response.setHomeDescription(homeService.getHomeDescription());
        response.setPosts(boardFreeService.getFreeLatest());
        response.setUsers(homeService.getUsersLatest(language));
        response.setComments(homeService.getBoardCommentsLatest());

        List<GalleryOnList> galleries =  homeService.getGalleriesLatest();

        // 사진 경로 붙히기.
        List<GalleryOnHome> galleriesOfHome = galleries.stream()
                .map(GalleryOnHome::new)
                .map(gallery -> {
                    gallery.setImageUrl(apiServerUrl + imagePath + gallery.getId());
                    gallery.setThumbnailUrl(apiServerUrl + thumbnailPath + gallery.getId());
                    return gallery;
                })
                .collect(Collectors.toList());

        response.setGalleries(galleriesOfHome);

        return response;
    }
}

