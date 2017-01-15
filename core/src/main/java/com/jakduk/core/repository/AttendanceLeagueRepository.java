package com.jakduk.core.repository;

import com.jakduk.core.model.db.AttendanceLeague;
import com.jakduk.core.model.db.Competition;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 10.
 * @desc     :
 */
public interface AttendanceLeagueRepository extends MongoRepository<AttendanceLeague, String>{

	Optional<AttendanceLeague> findOneById(String id);
	List<AttendanceLeague> findByCompetition(Competition competition, Sort sort);

	List<AttendanceLeague> findByCompetitionId(@Param("competitionId") String competitionId);

}
