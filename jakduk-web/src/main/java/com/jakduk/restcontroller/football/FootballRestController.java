package com.jakduk.restcontroller.football;

import java.util.List;
import java.util.Locale;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;

/**
 * Created by pyohwan on 16. 3. 20.
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
        String language = commonService.getLanguageCode(locale, lang);

        List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

        return footballClubs;
    }
}
