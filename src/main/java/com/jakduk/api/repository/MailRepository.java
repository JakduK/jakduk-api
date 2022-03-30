package com.jakduk.api.repository;

import com.jakduk.api.model.db.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MailRepository extends MongoRepository<Mail, String> {
    Optional<Mail> findOneById(String id);
}
