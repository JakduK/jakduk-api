package com.jakduk.api.home;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.repository.HomeDescriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@EnableConfigurationProperties
@Import({JakdukProperties.class, MongodbConfig.class})
public class HomeDescriptionRepositoryTests {

    @Autowired
    private HomeDescriptionRepository repository;

    @Test
    public void findOneByOrderByPriorityDesc() {
        Optional<HomeDescription> optHomeDescription = repository.findFirstByOrderByPriorityDesc();

        assertTrue(optHomeDescription.isPresent());
    }

}
