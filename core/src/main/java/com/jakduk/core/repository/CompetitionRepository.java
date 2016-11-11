package com.jakduk.core.repository;

import com.jakduk.core.model.db.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author pyohwan
 * 15. 12. 26 오후 11:01
 */
public interface CompetitionRepository extends MongoRepository<Competition, String> {
    Competition findOneByCode(String code);
}
