package com.jakduk.api.admin;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.util.CollectionUtils;

import com.jakduk.api.model.db.AttendanceLeague;
import com.jakduk.api.repository.AttendanceLeagueRepository;

/**
 * Created by pyohwan on 17. 1. 3.
 */

@DataMongoTest
public class AttendanceLeagueRepositoryTest {

	@Autowired
	private AttendanceLeagueRepository repository;

	@Test
	public void findByCompetitionId() {
		List<AttendanceLeague> attendanceLeagues = repository.findByCompetitionId("57600cef44ab251f9d64b4f8");
		assertTrue(!CollectionUtils.isEmpty(attendanceLeagues));

	}
}
