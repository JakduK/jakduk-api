package com.jakduk.repository;

import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.db.JakduSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 15. 12. 23.
 */
public interface JakduScheduleRepository extends MongoRepository<JakduSchedule, String> {
    Page<JakduSchedule> findAll(Pageable pageable);
}
