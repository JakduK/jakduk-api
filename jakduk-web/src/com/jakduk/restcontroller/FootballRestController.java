package com.jakduk.restcontroller;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

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
    public List<FootballClub> dataFootballClubs(Model model, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, null);

        List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

        return footballClubs;
    }
}
