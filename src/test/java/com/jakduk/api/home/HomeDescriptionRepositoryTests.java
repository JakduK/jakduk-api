package com.jakduk.api.home;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.repository.HomeDescriptionRepository;

@DataMongoTest
public class HomeDescriptionRepositoryTests {

	@Autowired
	private HomeDescriptionRepository repository;

	@Test
	public void findOneByOrderByPriorityDesc() {
		Optional<HomeDescription> optHomeDescription = repository.findFirstByOrderByPriorityDesc();

		assertTrue(optHomeDescription.isPresent());
	}

}
