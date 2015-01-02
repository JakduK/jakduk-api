package com.jakduk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.simple.UserOnHome;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */
public interface UserOnHomeRepository extends MongoRepository<UserOnHome, String> {
	Page<UserOnHome> findAll(Pageable pageable);
}
