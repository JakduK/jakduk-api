package com.jakduk.api.admin;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.model.db.AttendanceLeague;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.repository.AttendanceLeagueRepository;
import com.jakduk.api.repository.CompetitionRepository;

/**
 * Created by pyohwan on 17. 1. 3.
 */

@DataMongoTest
public class AttendanceLeagueRepositoryTest {

	@Autowired
	private AttendanceLeagueRepository repository;

	@Autowired
	private CompetitionRepository competitionRepository;

	private AttendanceLeague attendanceLeague;
	private Competition competition;

	@BeforeEach
	public void before() {

		competition = Competition.builder()
			.code("KL")
			.names(new ArrayList<LocalName>() {{
				add(new LocalName("ko", "K리그", "K리그"));
				add(new LocalName("en", "K LEAGUE", "K LEAGUE"));
			}})
			.build();

		competitionRepository.save(competition);

		attendanceLeague = AttendanceLeague.builder()
			.competition(Competition.builder()
				.id(competition.getId())
				.code("KL")
				.names(new ArrayList<LocalName>() {{
					add(new LocalName("ko", "K리그", "K리그"));
					add(new LocalName("en", "K LEAGUE", "K LEAGUE"));
				}})
				.build())
			.season(1983)
			.games(40)
			.total(419478)
			.average(20794)
			.numberOfClubs(5)
			.build();

		repository.save(attendanceLeague);
	}

	@Test
	public void findByCompetitionId() {
		List<AttendanceLeague> attendanceLeagues = repository.findByCompetitionId(competition.getId());
		assertEquals(1, attendanceLeagues.size());
		assertEquals(attendanceLeague, attendanceLeagues.get(0));
	}

	@AfterEach
	public void after() {
		repository.deleteById(attendanceLeague.getId());
		assertFalse(repository.findById(attendanceLeague.getId()).isPresent());
		assertEquals(0, repository.findAll().size());

		competitionRepository.deleteById(competition.getId());
		assertFalse(repository.findById(competition.getId()).isPresent());
		assertEquals(0, competitionRepository.findAll().size());
	}
}
