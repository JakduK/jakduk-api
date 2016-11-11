package com.jakduk.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.core.model.db.HomeDescription;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 15.
 * @desc     :
 */
public interface HomeDescriptionRepository extends MongoRepository<HomeDescription, String> {

}
