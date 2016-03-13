package com.jakduk.repository.jakdu;

import com.jakduk.model.db.Jakdu;
import com.jakduk.model.db.JakduSchedule;
import com.jakduk.model.embedded.CommonWriter;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 16. 3. 1.
 */
public interface JakduRepository extends MongoRepository<Jakdu, String> {

    Jakdu findByScheduleAndWriter(JakduSchedule jakduSchedule, CommonWriter writer);
}
