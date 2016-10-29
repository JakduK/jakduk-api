package com.jakduk.core.repository.jakdu;


import com.jakduk.core.model.db.Jakdu;
import com.jakduk.core.model.simple.JakduOnSchedule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author pyohwan
 * 16. 3. 1 오후 10:52
 */
public interface JakduRepository extends MongoRepository<Jakdu, String> {

    @Query(value="{'writer.userId' : ?0, 'schedule.$id' : ?1}")
    JakduOnSchedule findByUserIdAndWriter(String userId, ObjectId scheduleId);

}
