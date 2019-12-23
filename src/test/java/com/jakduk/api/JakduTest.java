package com.jakduk.api;


import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.model.db.JakduComment;
import com.jakduk.api.model.db.JakduSchedule;
import com.jakduk.api.repository.jakdu.JakduScheduleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pyohwan
 * 15. 12. 27 오후 11:57
 */

public class JakduTest extends ApiApplicationTests {

    @Autowired
    private JakdukDAO jakdukDAO;

    @Autowired
    private JakduScheduleRepository jakduScheduleRepository;

    @Test
    public void getSchedulesTest01() {
        List<ObjectId> ids = new ArrayList<ObjectId>();
        ids.add(new ObjectId("567e9af6e4b0c12e43d76157"));

        List<Competition> competitions = jakdukDAO.getCompetitions(ids, "ko");

        System.out.println("competitions=" + competitions);
    }

    @Test
    public void getSchedulesTest02() {
        Sort sort = Sort.by(Sort.Direction.ASC, "group", "date");
        Pageable pageable = PageRequest.of(0, 10, sort);

        List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll(pageable).getContent();

        System.out.println("jakduSchedules=" + jakduSchedules);
    }

    @Test
    public void getJakduComments() {
        List<JakduComment> comments = jakdukDAO.getJakduComments("567be7d8e4b06364b1618e0b", null);
        System.out.println("comments=" + comments);
    }

}
