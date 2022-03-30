package com.jakduk.api.repository;

import com.jakduk.api.model.db.HomeDescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 15.
 * @desc     :
 */
public interface HomeDescriptionRepository extends MongoRepository<HomeDescription, String> {

    Optional<HomeDescription> findFirstByOrderByPriorityDesc();

}
