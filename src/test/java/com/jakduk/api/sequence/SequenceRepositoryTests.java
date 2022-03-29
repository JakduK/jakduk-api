package com.jakduk.api.sequence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.common.Constants;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Sequence;
import com.jakduk.api.repository.SequenceRepository;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@DataMongoTest
public class SequenceRepositoryTests {

	@Autowired
	private SequenceRepository repository;

	@Test
	public void findCommentsCountByIds() {

		Sequence sequence = repository.findOneByName(Constants.SEQ_BOARD)
			.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));

		assertTrue(sequence.getSeq() > 0);
	}

}
