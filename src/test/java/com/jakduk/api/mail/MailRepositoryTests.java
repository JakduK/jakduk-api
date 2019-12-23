package com.jakduk.api.mail;

import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Mail;
import com.jakduk.api.repository.MailRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@Import({JakdukProperties.class, MongodbConfig.class})
public class MailRepositoryTests {

    @Autowired
    private MailRepository repository;

    private Mail mail;

    @BeforeEach
    public void setUp() {
        mail = new Mail();
        mail.setSubject("첫번째 메일 제목입니다.");
        mail.setTemplateName("mail/bulk01");

        repository.save(mail);
    }

    @Test
    public void findOneById() {
        Mail getMail = repository.findOneById(mail.getId())
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));

        assertEquals(getMail, mail);
    }

    @AfterEach
    public void after() {
        repository.deleteById(mail.getId());
    }

}
