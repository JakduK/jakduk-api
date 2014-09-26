package com.jakduk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.FootballClub;
import com.jakduk.model.simple.SupportFC;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */
public interface FootballClubRepository extends MongoRepository<FootballClub, String> {
	
	@Query(value="{'names.language' : ?0}", fields="{'fcId' : 1, 'names.$' : 1}")
	List<FootballClub> findByNamesLanguage(String language);
	
	@Query(value="{'id' : ?0}")
	SupportFC supportFCFindById(String id);
}
