package com.jakduk.batch.repository;

import com.jakduk.batch.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 9. 5.
 */
public interface AccountRepository extends MongoRepository<Account, String> {
}
