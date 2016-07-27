package com.jakduk.restcontroller.stats;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.AttendanceClub;
import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.model.db.Competition;
import com.jakduk.model.etc.SupporterCount;
import com.jakduk.restcontroller.stats.vo.SupportersDataResponse;
import com.jakduk.service.CommonService;
import com.jakduk.service.CompetitionService;
import com.jakduk.service.StatsService;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@Slf4j
@Api(value = "STATS", description = "통계 API")
@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private StatsService statsService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CompetitionService competitionService;

    @ApiOperation(value = "대회별 관중수 목록")
    @RequestMapping(value = "/league/attendances", method = RequestMethod.GET)
    public List<AttendanceLeague> getLeagueAttendances(@RequestParam(required = false) String competitionId,
                                                       @RequestParam(required = false) String competitionCode) {

        Competition competition = null;
        List<AttendanceLeague> leagueAttendances;

        Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));

        if (Objects.nonNull(competitionId)) {
            competition = competitionService.findCompetitionById(competitionId);
        } else if (Objects.nonNull(competitionCode)) {
            competition = competitionService.findCompetitionByCode(competitionCode);
        }

        if (Objects.isNull(competition)) {
            leagueAttendances = statsService.findLeagueAttendances(sort);
        } else {
            leagueAttendances = statsService.findLeagueAttendances(competition, sort);
        }

        return leagueAttendances;
    }

    @RequestMapping(value = "/attendance/club/{clubOrigin}", method = RequestMethod.GET)
    public List<AttendanceClub> getAttendancesClub(@PathVariable String clubOrigin,
                                                  HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(clubOrigin) || clubOrigin.isEmpty())
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));

        List<AttendanceClub> attendances = statsService.getAttendanceClub(locale, clubOrigin);

        return attendances;
    }

    @RequestMapping(value = "/attendance/season/{season}", method = RequestMethod.GET)
    public List<AttendanceClub> dataAttendanceSeason(@PathVariable Integer season,
                                     @RequestParam(required = false, defaultValue = CommonConst.K_LEAGUE_ABBREVIATION) String league){

        List<AttendanceClub> attendances = statsService.getAttendancesSeason(season, league);

        return attendances;
    }

    @RequestMapping(value = "/supporters", method = RequestMethod.GET)
    public SupportersDataResponse dataSupporter(HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, null);
        Map<String, Object> data = statsService.getSupportersData(language);

        return SupportersDataResponse.builder()
          .supporters((List<SupporterCount>) data.get("supporters"))
          .supportersTotal((Integer) data.get("supportersTotal"))
          .usersTotal((Integer) data.get("usersTotal"))
          .build();
    }
}
