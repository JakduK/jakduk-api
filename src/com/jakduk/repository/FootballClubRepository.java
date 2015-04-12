package com.jakduk.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */
public interface FootballClubRepository extends MongoRepository<FootballClub, String> {
	
	@Query(value="{'names.language' : ?0}", fields="{'active' : 1, 'origin' : 1, 'names.$' : 1}")
	List<FootballClub> findByNamesLanguage(String language, Pageable pageable);
}
