package com.jakduk.api.repository;

import com.jakduk.api.model.db.AttendanceClub;
import com.jakduk.api.model.db.FootballClubOrigin;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 18.
 * @desc     :
 */
public interface AttendanceClubRepository extends MongoRepository<AttendanceClub, String> {

	Optional<AttendanceClub> findOneById(String id);
	List<AttendanceClub> findByClub(FootballClubOrigin club, Sort sort);
	List<AttendanceClub> findBySeasonAndLeague(Integer season, String league, Sort sort);
	List<AttendanceClub> findBySeason(Integer season, Sort sort);

}
