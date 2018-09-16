package com.jakduk.api.user;

import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.repository.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@DataMongoTest
@Import({JakdukProperties.class, MongodbConfig.class})
public class UserRepositoryTests {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private User randomUser;

    @Before
    public void setUp(){
        User user = repository.findTopByOrderByIdAsc().get();
        LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(new ObjectId(user.getId()).getDate());
        Long hours = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
        LocalDateTime randomDate = localDateTime.plusMinutes(new Random().nextInt((int) (hours + 1)));
        randomUser = repository.findTopByIdLessThanEqualOrderByIdDesc(new ObjectId(DateUtils.localDateTimeToDate(randomDate))).get();
    }

    @Test
    public void existEmailTest() {
        if (StringUtils.isBlank(randomUser.getEmail())) {
            log.warn("randomUser email is empty : {}", randomUser);
        } else {
            Optional<UserProfile> mustFind = userProfileRepository.findOneByEmail(randomUser.getEmail());
            Assert.assertTrue(mustFind.isPresent());

            Optional<UserProfile> mustNotFind = userProfileRepository.findByNEIdAndEmail(randomUser.getId(), randomUser.getEmail());
            Assert.assertFalse(mustNotFind.isPresent());
        }
    }

    @Test
    public void existUserNameTest() {

        Optional<UserProfile> mustFind = userProfileRepository.findOneByUsername(randomUser.getUsername());
        Assert.assertTrue(mustFind.isPresent());

        Optional<UserProfile> mustNotFind = userProfileRepository.findByNEIdAndUsername(randomUser.getId(), randomUser.getUsername());
        Assert.assertFalse(mustNotFind.isPresent());
    }

}
