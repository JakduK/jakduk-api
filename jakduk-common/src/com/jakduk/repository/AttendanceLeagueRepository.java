package com.jakduk.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.AttendanceLeague;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 10.
 * @desc     :
 */
public interface AttendanceLeagueRepository extends MongoRepository<AttendanceLeague, String>{
	List<AttendanceLeague> findByLeague(String league, Sort sort);

}
