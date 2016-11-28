package com.jakduk.api.restcontroller.football;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.FootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

/**
 * @author pyohwan
 * 16. 3. 20 오후 11:26
 */

@RestController
@RequestMapping("/api/football")
public class FootballRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FootballService footballService;

    @RequestMapping(value = "/clubs", method = RequestMethod.GET)
    public List<FootballClub> dataFootballClubs(@RequestParam String lang, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = CoreUtils.getLanguageCode(locale, lang);

        return footballService.getFootballClubs(language, CoreConst.CLUB_TYPE.FOOTBALL_CLUB, CoreConst.NAME_TYPE.fullName);
    }
}
