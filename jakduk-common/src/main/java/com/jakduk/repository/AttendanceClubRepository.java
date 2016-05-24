package com.jakduk.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.AttendanceClub;
import com.jakduk.model.db.FootballClubOrigin;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 18.
 * @desc     :
 */
public interface AttendanceClubRepository extends MongoRepository<AttendanceClub, String>{
	List<AttendanceClub> findByClub(FootballClubOrigin club, Sort sort);
	List<AttendanceClub> findBySeasonAndLeague(Integer season, String league, Sort sort);
	List<AttendanceClub> findBySeason(Integer season, Sort sort);
}
