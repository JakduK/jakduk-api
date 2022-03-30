package com.jakduk.api.repository.jakdu;

import com.jakduk.api.model.db.JakduComment;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author pyohwan
 * 16. 3. 13 오후 11:05
 */
public interface JakduCommentRepository extends MongoRepository<JakduComment, String> {
	Integer countByJakduScheduleId(String jakduScheduleId);
}
