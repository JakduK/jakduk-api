package com.jakduk.api.sequence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Sequence;
import com.jakduk.api.repository.SequenceRepository;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@DataMongoTest
public class SequenceRepositoryTests {

	@Autowired
	private SequenceRepository repository;

	private Sequence boardSequence;
	private Sequence jakdukScheduleGroupSequence;
	@BeforeEach
	public void before() {
		boardSequence = Sequence.builder()
			.seq(371)
			.name(Constants.SEQ_BOARD)
			.build();

		jakdukScheduleGroupSequence = Sequence.builder()
			.seq(1)
			.name(Constants.SEQ_JAKDU_SCHEDULE_GROUP)
			.build();

		repository.save(boardSequence);
		repository.save(jakdukScheduleGroupSequence);
	}
	@Test
	public void findOneByName() {
		Sequence sequence = repository.findOneByName(Constants.SEQ_BOARD).get();
		assertEquals(boardSequence, sequence);
	}

	@AfterEach
	public void after() {
		repository.deleteById(boardSequence.getId());
		assertFalse(repository.findById(boardSequence.getId()).isPresent());
		repository.deleteById(jakdukScheduleGroupSequence.getId());
		assertEquals(0, repository.findAll().size());
	}

}
