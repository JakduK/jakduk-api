package com.jakduk.restcontroller.home;

import com.jakduk.common.ApiUtils;
import com.jakduk.exception.SuccessButNoContentException;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.service.CommonService;
import com.jakduk.service.HomeService;
import com.jakduk.restcontroller.home.vo.HomeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@RestController
@RequestMapping("/api")
public class HomeRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private HomeService homeService;

    // 백과사전 가져오기.
    @RequestMapping(value = "/home/encyclopedia", method = RequestMethod.GET)
    public Encyclopedia getEncyclopedia(@RequestParam(required = false) String lang,
                                        HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, lang);

        Encyclopedia encyclopedia = homeService.getEncyclopedia(language);

        if (Objects.isNull(encyclopedia))
            throw new SuccessButNoContentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

        return encyclopedia;
    }

    // 홈에서 보여줄 각종 최근 데이터 가져오기.
    @RequestMapping(value = "/home/latest", method = RequestMethod.GET)
    public HomeResponse dataLatest(@RequestParam(required = false) String lang,
                           HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, lang);

        HomeResponse response = new HomeResponse();
        response.setPosts(homeService.getBoardLatest());
        response.setUsers(homeService.getUsersLatest(language));
        response.setGalleries(homeService.getGalleriesLatest());
        response.setComments(homeService.getBoardCommentsLatest());
        response.setHomeDescription(homeService.getHomeDescription());

        return response;
    }

    // 홈에서 보여줄 각종 최근 데이터 가져오기.
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Map<String, Object> test(HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("URL", ApiUtils.buildFullRequestUrl(request, "/hello"));
        response.put("getServletPath", request.getServletPath());
        response.put("getServerPort", request.getServerPort());
        response.put("getRequestURL", request.getRequestURL());

        return response;
    }
}

