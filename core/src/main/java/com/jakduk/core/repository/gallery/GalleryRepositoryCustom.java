package com.jakduk.core.repository.gallery;

import com.jakduk.core.model.elasticsearch.ESGallery;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface GalleryRepositoryCustom {

    // 기준 Gallery ID 이상의 Gallery 목록을 가져온다.
    List<ESGallery> findGalleriesGreaterThanId(ObjectId objectId, Integer limit);
}
