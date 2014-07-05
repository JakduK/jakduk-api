package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.Encyclopedia;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */
public interface EncyclopediaRepository extends MongoRepository<Encyclopedia, String> {
	
	Encyclopedia findOneBySeqAndLanguage(Integer seq, Integer language);
	Integer countByLanguage(Integer language);

}
