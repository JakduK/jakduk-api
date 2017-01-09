package com.jakduk.core.repository;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.FootballClubOrigin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */
public interface FootballClubOriginRepository extends MongoRepository<FootballClubOrigin, String> {

	Optional<FootballClubOrigin> findOneByName(String name);
	List<FootballClubOrigin> findByClubType(CoreConst.CLUB_TYPE clubType);
}
