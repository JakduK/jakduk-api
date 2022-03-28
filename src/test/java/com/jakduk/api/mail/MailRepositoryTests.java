package com.jakduk.api.mail;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Mail;
import com.jakduk.api.repository.MailRepository;

@DataMongoTest
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
