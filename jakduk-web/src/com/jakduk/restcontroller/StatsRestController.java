package com.jakduk.restcontroller;

import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.model.web.stats.AttendanceClubResponse;
import com.jakduk.service.CommonService;
import com.jakduk.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@RestController
@RequestMapping("/api/stats")
@Slf4j
public class StatsRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private StatsService statsService;

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/attendance/league/{league}", method = RequestMethod.GET)
    public List<AttendanceLeague> dataLeagueAttendance(@PathVariable String league) {

        List<AttendanceLeague> attendanceList = statsService.getAttendanceLeague(league);

        return attendanceList;
    }

    @RequestMapping(value = "/attendance/club/{clubOrigin}", method = RequestMethod.GET)
    public AttendanceClubResponse getAttendanceClub(@PathVariable String clubOrigin,
                                   HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(clubOrigin) || clubOrigin.isEmpty())
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.msg.invalid.parameter.exception"));

        AttendanceClubResponse response = statsService.getAttendanceClubData(locale, clubOrigin);

        return response;
    }
}
