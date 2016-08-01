package com.jakduk.api;

import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.model.db.JakduComment;
import com.jakduk.api.model.db.JakduSchedule;
import com.jakduk.api.repository.jakdu.JakduRepository;
import com.jakduk.api.repository.jakdu.JakduScheduleRepository;
import com.jakduk.api.util.AbstractSpringTest;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pyohwan on 15. 12. 27.
 */

public class JakduTest extends AbstractSpringTest {

    @Autowired
    private  JakdukDAO jakdukDAO;

    @Autowired
    private JakduScheduleRepository jakduScheduleRepository;

    @Autowired
    private JakduRepository jakduRepository;

    @Test
    public void getSchedulesTest01() {
        List<ObjectId> ids = new ArrayList<ObjectId>();
        ids.add(new ObjectId("567e9af6e4b0c12e43d76157"));

        List<Competition> competitions = jakdukDAO.getCompetitions(ids, "ko");

        System.out.println("competitions=" + competitions);
    }

    @Test
    public void getSchedulesTest02() {
        Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("group", "date"));
        Pageable pageable = new PageRequest(0, 10, sort);

        List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll(pageable).getContent();

        System.out.println("jakduSchedules=" + jakduSchedules);
    }

    @Test
    public void getJakduComments() {
        List<JakduComment> comments = jakdukDAO.getJakduComments("567be7d8e4b06364b1618e0b", null);
        System.out.println("comments=" + comments);
    }

    @Test
    public void 작두일정가져오기() {

        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne("567be7d8e4b06364b1618e0b");

        System.out.println(jakduSchedule);

        System.out.println(jakduRepository.findByUserIdAndWriter("566d68d5e4b0dfaaa5b98685", new ObjectId("567be7d8e4b06364b1618e0b")));


    }
}
