package com.jakduk.repository.jakdu;

import com.jakduk.model.db.JakduComment;
import com.jakduk.model.embedded.BoardItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by pyohwan on 16. 3. 13.
 */
public interface JakduCommentRepository extends MongoRepository<JakduComment, String> {
    Integer countByJakduScheduleId(String jakduScheduleId);
}
