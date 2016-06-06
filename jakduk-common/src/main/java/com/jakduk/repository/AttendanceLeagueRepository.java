package com.jakduk.repository;

import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.model.db.Competition;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 10.
 * @desc     :
 */
public interface AttendanceLeagueRepository extends MongoRepository<AttendanceLeague, String>{
	List<AttendanceLeague> findByCompetition(Competition competition, Sort sort);

}
