package com.jakduk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */
public interface FootballClubRepository extends MongoRepository<FootballClub, String> {
	
	List<FootballClub> findAll();
}
