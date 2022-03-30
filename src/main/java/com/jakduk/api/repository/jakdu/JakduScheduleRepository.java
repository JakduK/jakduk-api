package com.jakduk.api.repository.jakdu;

import com.jakduk.api.model.db.JakduSchedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author pyohwan
 * 15. 12. 23 오후 11:05
 */
public interface JakduScheduleRepository extends MongoRepository<JakduSchedule, String> {
	Page<JakduSchedule> findAll(Pageable pageable);

	List<JakduSchedule> findAll(Sort sort);

	List<JakduSchedule> findByTimeUpOrderByDateAsc(boolean timeUp);
}
