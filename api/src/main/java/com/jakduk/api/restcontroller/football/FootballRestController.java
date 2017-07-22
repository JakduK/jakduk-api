package com.jakduk.api.restcontroller.football;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.service.FootballService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * @author pyohwan
 * 16. 3. 20 오후 11:26
 */

@Api(tags = "Football", description = "축구단 API")
@RestController
@RequestMapping("/api/football")
public class FootballRestController {

    @Autowired
    private FootballService footballService;

    @ApiOperation(value = "축구단 목록")
    @RequestMapping(value = "/clubs", method = RequestMethod.GET)
    public List<FootballClub> getFootballClubs(@RequestParam String lang) {

        String language = CoreUtils.getLanguageCode(lang);

        return footballService.getFootballClubs(language, CoreConst.CLUB_TYPE.FOOTBALL_CLUB, CoreConst.NAME_TYPE.fullName);
    }
}
