package com.jakduk.api.repository;

import com.jakduk.api.model.db.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author pyohwan
 * 15. 12. 26 오후 11:01
 */
public interface CompetitionRepository extends MongoRepository<Competition, String> {

    Optional<Competition> findOneById(String id);
    Competition findOneByCode(String code);

}
