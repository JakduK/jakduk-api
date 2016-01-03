package com.jakduk.service;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.*;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.model.web.JakduWriteList;
import com.jakduk.repository.JakduScheduleRepository;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by pyohwan on 15. 12. 26.
 */

@Service
public class JakduService {

    @Autowired
    private JakdukDAO jakdukDAO;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Autowired
    private JakduScheduleRepository jakduScheduleRepository;

    public void getSchedule(Model model, Locale locale) {

        model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
    }

    public void getDataScheduleList(Model model, String language, int page, int size) {

        Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
        Pageable pageable = new PageRequest(page - 1, size, sort);

        Set<ObjectId> fcIds = new HashSet<>();
        Set<ObjectId> competitionIds = new HashSet<>();
        List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll(pageable).getContent();

        for (JakduSchedule jakduSchedule : jakduSchedules) {
            fcIds.add(new ObjectId(jakduSchedule.getHome().getId()));
            fcIds.add(new ObjectId(jakduSchedule.getAway().getId()));
            if (jakduSchedule.getCompetition() != null)
                competitionIds.add(new ObjectId(jakduSchedule.getCompetition().getId()));
        }

        Map<String, LocalName> fcNames = new HashMap<>();
        Map<String, LocalName> competitionNames = new HashMap<>();

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubList(new ArrayList<>(fcIds), language, CommonConst.NAME_TYPE.fullName);
        List<Competition> competitions = jakdukDAO.getCompetitionList(new ArrayList<>(competitionIds), language);

        for (FootballClub fc : footballClubs) {
            fcNames.put(fc.getOrigin().getId(), fc.getNames().get(0));
        }

        for (Competition competition : competitions) {
            competitionNames.put(competition.getId(), competition.getNames().get(0));
        }

        try {
            model.addAttribute("fcNames", new ObjectMapper().writeValueAsString(fcNames));
            model.addAttribute("competitionNames", new ObjectMapper().writeValueAsString(competitionNames));
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("schedules", jakduSchedules);
    }

    public void getWrite(Model model, Locale locale) {

        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();
        String accountUsername = principal.getUsername();
        String accountType = principal.getType();

        if (accountId == null) {
            return;
        }

        CommonWriter writer = new CommonWriter();
        writer.setUserId(accountId);
        writer.setUsername(accountUsername);
        writer.setType(accountType);

        String language = commonService.getLanguageCode(locale, null);

        Set<ObjectId> fcIds = new HashSet<>();
        JakduWriteList jakduWriteList = new JakduWriteList();
        List<Jakdu> jakdus = new ArrayList<>();
        Set<ObjectId> competitionIds = new HashSet<>();
        List<JakduSchedule> schedules = jakduScheduleRepository.findByTimeUpOrderByDateAsc(false);

        for (JakduSchedule jakduSchedule : schedules) {
            Jakdu jakdu = new Jakdu();
            jakdu.setSchedule(jakduSchedule);
            jakdu.setWriter(writer);
            jakdus.add(jakdu);

            fcIds.add(new ObjectId(jakduSchedule.getHome().getId()));
            fcIds.add(new ObjectId(jakduSchedule.getAway().getId()));
            if (jakduSchedule.getCompetition() != null)
                competitionIds.add(new ObjectId(jakduSchedule.getCompetition().getId()));
        }

        jakduWriteList.setJakdus(jakdus);

        Map<String, LocalName> fcNames = new HashMap<>();
        Map<String, LocalName> competitionNames = new HashMap<>();

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubList(new ArrayList<>(fcIds), language, CommonConst.NAME_TYPE.fullName);
        List<Competition> competitions = jakdukDAO.getCompetitionList(new ArrayList<>(competitionIds), language);

        for (FootballClub fc : footballClubs) {
            fcNames.put(fc.getOrigin().getId(), fc.getNames().get(0));
        }

        for (Competition competition : competitions) {
            competitionNames.put(competition.getId(), competition.getNames().get(0));
        }

        model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
        model.addAttribute("jakduWriteList", jakduWriteList);
        model.addAttribute("fcNames", fcNames);
        model.addAttribute("competitionNames", competitionNames);
    }
}
