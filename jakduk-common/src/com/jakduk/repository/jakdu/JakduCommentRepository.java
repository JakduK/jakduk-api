package com.jakduk.repository.jakdu;

import com.jakduk.model.db.JakduComment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyohwan on 16. 3. 13.
 */
public interface JakduCommentRepository extends MongoRepository<JakduComment, String> {
}
