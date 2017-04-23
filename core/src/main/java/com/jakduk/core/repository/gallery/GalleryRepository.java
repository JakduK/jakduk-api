package com.jakduk.core.repository.gallery;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.Gallery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */
public interface GalleryRepository extends MongoRepository<Gallery, String>, GalleryRepositoryCustom {

	Optional<Gallery> findOneById(String id);
	Optional<Gallery> findOneByHashAndStatusStatus(String hash, CoreConst.GALLERY_STATUS_TYPE status);
	
	@Query(value="{'status.status' : 'use'}")
	List<Gallery> findAll();

	List<Gallery> findByIdIn(List<String> ids);

}
