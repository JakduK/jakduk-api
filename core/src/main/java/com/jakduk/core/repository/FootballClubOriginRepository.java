package com.jakduk.core.repository;

import com.jakduk.core.common.CoreConst;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.core.model.db.FootballClubOrigin;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */
public interface FootballClubOriginRepository extends MongoRepository<FootballClubOrigin, String> {
	FootballClubOrigin findByName(String name);
	List<FootballClubOrigin> findByClubType(CoreConst.CLUB_TYPE clubType);
}
