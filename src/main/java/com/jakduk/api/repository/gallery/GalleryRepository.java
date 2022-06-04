package com.jakduk.api.repository.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Gallery;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */
public interface GalleryRepository extends MongoRepository<Gallery, String>, GalleryRepositoryCustom {

	Optional<Gallery> findOneById(String id);

	Optional<Gallery> findOneByHashAndStatusStatus(String hash, Constants.GALLERY_STATUS_TYPE status);

	List<Gallery> findByIdIn(List<String> ids);

}
