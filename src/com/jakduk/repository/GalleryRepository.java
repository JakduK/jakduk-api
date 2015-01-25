package com.jakduk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.model.db.Gallery;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */
public interface GalleryRepository extends MongoRepository<Gallery, String>{

}
