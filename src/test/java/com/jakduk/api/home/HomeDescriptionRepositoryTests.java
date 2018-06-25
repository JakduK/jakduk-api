package com.jakduk.api.home;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.repository.HomeDescriptionRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataMongoTest
@Import({JakdukProperties.class, MongodbConfig.class})
public class HomeDescriptionRepositoryTests {

    @Autowired
    private HomeDescriptionRepository repository;

    @Test
    public void findOneByOrderByPriorityDesc() {
        Optional<HomeDescription> optHomeDescription = repository.findFirstByOrderByPriorityDesc();

        Assert.assertTrue(optHomeDescription.isPresent());
    }

}
