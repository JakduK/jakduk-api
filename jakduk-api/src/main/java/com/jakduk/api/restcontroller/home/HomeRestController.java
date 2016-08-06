package com.jakduk.api.restcontroller.home;

import com.jakduk.api.restcontroller.home.vo.GalleryOnHome;
import com.jakduk.api.restcontroller.home.vo.HomeResponse;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Encyclopedia;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 20 오후 9:13
 */

@Api(tags = "홈", description = "홈 관련")
@RestController
@RequestMapping("/api")
public class HomeRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private HomeService homeService;

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${gallery.image.path}")
    private String imagePath;

    @Value("${gallery.thumbnail.path}")
    private String thumbnailPath;

    @ApiOperation(value = "백과사전 가져오기", produces = "application/json", response = Encyclopedia.class)
    @RequestMapping(value = "/home/encyclopedia", method = RequestMethod.GET)
    public Encyclopedia getEncyclopedia(@RequestParam(required = false) String lang,
                                        HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, lang);

        Encyclopedia encyclopedia = homeService.getEncyclopedia(language);

        if (Objects.isNull(encyclopedia))
            throw new ServiceException(ServiceError.NOT_FOUND);

        return encyclopedia;
    }

    @ApiOperation(value = "홈에서 보여줄 각종 최근 데이터 가져오기", produces = "application/json", response = HomeResponse.class)
    @RequestMapping(value = "/home/latest", method = RequestMethod.GET)
    public HomeResponse dataLatest(@RequestParam(required = false) String lang,
                                   HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, lang);

        HomeResponse response = new HomeResponse();
        response.setHomeDescription(homeService.getHomeDescription());
        response.setPosts(homeService.getBoardLatest());
        response.setUsers(homeService.getUsersLatest(language));
        response.setComments(homeService.getBoardCommentsLatest());

        List<GalleryOnList> galleries =  homeService.getGalleriesLatest();

        // 사진 경로 붙히기.
        Consumer<GalleryOnHome> applyImageUrl = gallery -> {
            gallery.setImageUrl(apiServerUrl + imagePath + gallery.getId());
            gallery.setThumbnailUrl(apiServerUrl + thumbnailPath + gallery.getId());
        };

        List<GalleryOnHome> cvtGalleries = galleries.stream().map(GalleryOnHome::new).collect(Collectors.toList());
        cvtGalleries.forEach(applyImageUrl);

        response.setGalleries(cvtGalleries);

        return response;
    }
}

