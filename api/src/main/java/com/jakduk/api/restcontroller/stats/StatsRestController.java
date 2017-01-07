package com.jakduk.api.restcontroller.stats;

import com.jakduk.api.restcontroller.stats.vo.SupportersDataResponse;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.AttendanceClub;
import com.jakduk.core.model.db.AttendanceLeague;
import com.jakduk.core.model.db.Competition;
import com.jakduk.core.model.etc.SupporterCount;
import com.jakduk.core.service.CompetitionService;
import com.jakduk.core.service.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author pyohwan
 * 16. 3. 20 오후 11:25
 */

@Slf4j
@Api(tags = "통계", description = "통계 API")
@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private StatsService statsService;

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
            competition = competitionService.findOneById(competitionId);
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
            throw new IllegalArgumentException(CoreUtils.getExceptionMessage("exception.invalid.parameter"));

        List<AttendanceClub> attendances = statsService.getAttendanceClub(locale, clubOrigin);

        return attendances;
    }

    @RequestMapping(value = "/attendance/season/{season}", method = RequestMethod.GET)
    public List<AttendanceClub> dataAttendanceSeason(@PathVariable Integer season,
                                     @RequestParam(required = false, defaultValue = CoreConst.K_LEAGUE_ABBREVIATION) String league){

        List<AttendanceClub> attendances = statsService.getAttendancesSeason(season, league);

        return attendances;
    }

    @RequestMapping(value = "/supporters", method = RequestMethod.GET)
    public SupportersDataResponse dataSupporter(HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = CoreUtils.getLanguageCode(locale, null);
        Map<String, Object> data = statsService.getSupportersData(language);

        return SupportersDataResponse.builder()
          .supporters((List<SupporterCount>) data.get("supporters"))
          .supportersTotal((Integer) data.get("supportersTotal"))
          .usersTotal((Integer) data.get("usersTotal"))
          .build();
    }
}
