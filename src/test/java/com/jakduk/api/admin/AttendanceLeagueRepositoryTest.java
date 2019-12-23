package com.jakduk.api.admin;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.repository.AttendanceLeagueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pyohwan on 17. 1. 3.
 */
public class AttendanceLeagueRepositoryTest extends ApiApplicationTests {

    @Autowired
    private AttendanceLeagueRepository sut;

    @Test
    public void findByCompetitionId() {
        sut.findByCompetitionId("57600cef44ab251f9d64b4f8");
    }
}
