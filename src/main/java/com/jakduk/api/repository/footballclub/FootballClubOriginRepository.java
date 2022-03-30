package com.jakduk.api.repository.footballclub;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.FootballClubOrigin;

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

	Optional<FootballClubOrigin> findOneById(String id);

	Optional<FootballClubOrigin> findOneByName(String name);

	List<FootballClubOrigin> findByClubType(Constants.CLUB_TYPE clubType);
}
