package com.jakduk.repository;

import com.jakduk.model.db.Competition;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 15. 12. 26.
 */
public interface CompetitionRepository extends MongoRepository<Competition, String> {
    Competition findOneByCode(String code);
}
