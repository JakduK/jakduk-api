package com.jakduk.api.repository;

import com.jakduk.api.model.db.Encyclopedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */
public interface EncyclopediaRepository extends MongoRepository<Encyclopedia, String> {
	
	List<Encyclopedia> findListByLanguage(String language);

}
