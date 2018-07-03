package com.jakduk.api.mail;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Mail;
import com.jakduk.api.repository.MailRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
@Import({JakdukProperties.class, MongodbConfig.class})
public class MailRepositoryTests {

    @Autowired
    private MailRepository repository;

    private Mail mail;

    @Before
    public void setUp() {
        mail = Mail.builder()
                .subject("첫번째 메일 제목입니다.")
                .templateName("mail/bulk01")
                .build();

        repository.save(mail);
    }

    @Test
    public void findOneById() {
        Mail getMail = repository.findOneById(mail.getId())
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));

        Assert.assertEquals(getMail, mail);
    }

    @After
    public void after() {
        repository.deleteById(mail.getId());
    }

}
