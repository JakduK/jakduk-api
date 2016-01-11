package com.jakduk.repository;

import com.jakduk.model.db.JakduScheduleGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 16. 1. 10.
 */
public interface JakduScheduleGroupRepository extends MongoRepository<JakduScheduleGroup, String> {
    JakduScheduleGroup findBySeq(int seq);
}
