package com.jakduk.repository.jakdu;

import com.jakduk.model.db.Jakdu;
import com.jakduk.model.db.JakduSchedule;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.simple.JakduOnSchedule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by pyohwan on 16. 3. 1.
 */
public interface JakduRepository extends MongoRepository<Jakdu, String> {

    @Query(value="{'writer.userId' : ?0, 'schedule.$id' : ?1}")
    JakduOnSchedule findByUserIdAndWriter(String userId, ObjectId scheduleId);

}
