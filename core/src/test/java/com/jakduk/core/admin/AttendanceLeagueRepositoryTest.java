package com.jakduk.core.admin;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.repository.AttendanceLeagueRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pyohwan on 17. 1. 3.
 */
public class AttendanceLeagueRepositoryTest extends CoreApplicationTests {

    @Autowired
    private AttendanceLeagueRepository sut;

    @Test
    public void findByCompetitionId() {
        sut.findByCompetitionId("57600cef44ab251f9d64b4f8");
    }
}
