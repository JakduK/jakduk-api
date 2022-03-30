package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.model.aggregate.SupporterCount;
import com.jakduk.api.model.db.AttendanceClub;
import com.jakduk.api.model.db.AttendanceLeague;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.restcontroller.vo.stats.SupportersDataResponse;
import com.jakduk.api.service.CompetitionService;
import com.jakduk.api.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 통계 API
 *
 * @author pyohwan
 * 16. 3. 20 오후 11:25
 */

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatsService statsService;

    @Autowired
    private CompetitionService competitionService;

    // 대회별 관중수 목록
    @RequestMapping(value = "/league/attendances", method = RequestMethod.GET)
    public List<AttendanceLeague> getLeagueAttendances(@RequestParam(required = false) String competitionId,
                                                       @RequestParam(required = false) String competitionCode) {

        Competition competition = null;
        List<AttendanceLeague> leagueAttendances;

        if (Objects.nonNull(competitionId)) {
            competition = competitionService.findOneById(competitionId);
        } else if (Objects.nonNull(competitionCode)) {
            competition = competitionService.findCompetitionByCode(competitionCode);
        }

        if (Objects.isNull(competition)) {
            leagueAttendances = statsService.findLeagueAttendances(Constants.SORT_BY_ID_ASC);
        } else {
            leagueAttendances = statsService.findLeagueAttendances(competition, Constants.SORT_BY_ID_ASC);
        }

        return leagueAttendances;
    }

    // 구단별 관중수 목록
    @RequestMapping(value = "/attendance/club/{clubOrigin}", method = RequestMethod.GET)
    public List<AttendanceClub> getAttendancesClubs(@PathVariable String clubOrigin) {

        return statsService.getAttendanceClub(clubOrigin);
    }

    // 연도별 관중수 목록
    @RequestMapping(value = "/attendance/season/{season}", method = RequestMethod.GET)
    public List<AttendanceClub> getAttendanceSeason(@PathVariable Integer season,
                                                    @RequestParam(required = false, defaultValue = Constants.K_LEAGUE_ABBREVIATION) String league){

        return statsService.getAttendancesSeason(season, league);
    }

    @RequestMapping(value = "/supporters", method = RequestMethod.GET)
    public SupportersDataResponse dataSupporter() {

        String language = JakdukUtils.getLanguageCode();
        Map<String, Object> data = statsService.getSupportersData(language);

        return new SupportersDataResponse() {{
            setSupporters((List<SupporterCount>) data.get("supporters"));
            setSupportersTotal((Integer) data.get("supportersTotal"));
            setUsersTotal((Integer) data.get("usersTotal"));
        }};
    }
}
