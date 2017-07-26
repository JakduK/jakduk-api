package com.jakduk.api.repository.gallery;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.elasticsearch.EsGallery;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface GalleryRepositoryCustom {

    // 기준 Gallery ID 이상의 Gallery 목록을 가져온다.
    List<EsGallery> findGalleriesGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * 사진첩 보기의 앞, 뒤 사진을 가져온다.
     */
    List<Gallery> findGalleriesById(ObjectId id, JakdukConst.CRITERIA_OPERATOR operator, Integer limit);

    /**
     * ItemID와 FromType에 해당하는 Gallery 목록을 가져온다.
     */
    List<Gallery> findByItemIdAndFromType(ObjectId itemId, JakdukConst.GALLERY_FROM_TYPE fromType, Integer limit);

}
