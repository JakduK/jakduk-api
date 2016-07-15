package com.jakduk.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.model.db.Gallery;
import com.jakduk.model.simple.GalleryOnList;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */
public interface GalleryRepository extends MongoRepository<Gallery, String>{
	
	@Query(value="{'status.status' : 'use'}")
	Page<GalleryOnList> findList(Pageable pageable);
	
	@Query(value="{'status.status' : 'use'}")
	List<Gallery> findAll();

	List<Gallery> findByIdIn(List<String> ids);
}
